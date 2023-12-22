package cn.windery.sentinel.slots.degrade.circuitbreak;


import cn.windery.sentinel.Context;
import cn.windery.sentinel.WindowWrap;
import cn.windery.sentinel.slots.degrade.DegradeRule;
import cn.windery.sentinel.slots.statistic.LeapArray;
import cn.windery.sentinel.util.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.concurrent.atomic.LongAdder;

public class ResponseTimeCircuitBreaker extends AbstractCircuitBreaker {

    private static final Logger log = LoggerFactory.getLogger(ResponseTimeCircuitBreaker.class);


    private final LeapArray<SlowRequestCounter> slowRequestLeapArray;

    public ResponseTimeCircuitBreaker(DegradeRule rule) {
        super(rule);
        slowRequestLeapArray = new SlowRequestLeapArray(1, rule.getWindowLengthInMs());
    }

    @Override
    public void onRequestComplete(Context context) {

        long start = context.getEntryTime();
        long rt = System.currentTimeMillis() - start;


        SlowRequestCounter counter = slowRequestLeapArray.currentWindow(System.currentTimeMillis()).getValue();
        counter.totalCount.increment();
        if (rt > getMaxRt()) {
            counter.slowCount.increment();
        }

        if (currentState() == State.OPEN) {
            return;
        }

        if (currentState() == State.HALF_OPEN && context.isRecoverRequest()) {
            recoverRequestCounter.addFinished();
            if (rt > getMaxRt()) {
                recoverRequestCounter.addFail();
            }
            AssertUtil.isTrue(rule.getRecoverRequests() > 0, "recover pass requests must be positive");
            if (recoverRequestCounter.getFinished() >= rule.getRecoverRequests()) {
                double recoverThreshold = recoverRequestCounter.getFail() * 1.0 / recoverRequestCounter.getFinished();
                if (recoverThreshold < rule.getThreshold()) {
                    log.info("resource {} circuit breaker recover success, recover pass count: {}, recover request count: {},  maxRt: {}, request slow rate threshold: {}, recover requests slow rate: {}",
                            rule.getResource(), rule.getRecoverRequests(), recoverRequestCounter, getMaxRt(), getThreshold(), recoverThreshold);
                    fromHalfOpenToClosed();
                } else {
                    log.info("resource {} circuit breaker recover fail, recover pass count: {}, recover request count: {},  maxRt: {}, request slow rate threshold: {}, recover requests slow rate: {}",
                            rule.getResource(), rule.getRecoverRequests(), recoverRequestCounter, getMaxRt(), getThreshold(), recoverThreshold);
                    fromHalfOpenToOpen();
                }
            }
            context.setRecoverRequest(false);
            return;
        }

        if (counter.totalCount.longValue() < getMinRequests()) {
            return;
        }

        double slowRatio = counter.slowCount.doubleValue() / counter.totalCount.doubleValue();
        if (slowRatio > getThreshold()) {
            log.info("resource {} circuit breaker slow request ratio is {}, threshold is {}, counter: {}",
                    rule.getResource(), slowRatio, getThreshold(), counter);
            fromClosedToOpen();
        }

    }

    static class SlowRequestCounter {

        private final LongAdder slowCount = new LongAdder();
        private final LongAdder totalCount = new LongAdder();


        public void reset() {
            slowCount.reset();
            totalCount.reset();
        }

        @Override
        public String toString() {
            return "SlowRequestCounter{" +
                    "slowCount=" + slowCount +
                    ", totalCount=" + totalCount +
                    '}';
        }
    }

    static class SlowRequestLeapArray extends LeapArray<SlowRequestCounter> {

        public SlowRequestLeapArray(int sampleCount, int intervalInMs) {
            super(sampleCount, intervalInMs);
        }

        @Override
        public WindowWrap<SlowRequestCounter> newEmptyBucket(long windowStart, int windowLengthInMs) {
            return new WindowWrap<>(windowStart, windowLengthInMs, new SlowRequestCounter());
        }

        @Override
        protected void resetWindowTo(WindowWrap<SlowRequestCounter> windowWrap, long startTime) {
            windowWrap.resetTo(startTime);
            windowWrap.getValue().reset();
        }
    }

    @Override
    public Object statistic() {
        HashMap<Object, Object> content = new HashMap<>();
        SlowRequestCounter counter = slowRequestLeapArray.currentWindow(System.currentTimeMillis()).getValue();
        long total = counter.totalCount.longValue();
        content.put("totalCount", total);
        content.put("slowCount", counter.slowCount.longValue());
        content.put("slowRatio", total > 0 ? (counter.slowCount.doubleValue() / counter.totalCount.doubleValue()) : 0);
        content.put("recoverPass", recoverRequestCounter.getPass());
        content.put("recoverFail", recoverRequestCounter.getFail());
        content.put("recoverFinished", recoverRequestCounter.getFinished());
        return content;
    }


}
package cn.windery.sentinel.slots.degrade.circuitbreak;


import cn.windery.sentinel.Context;
import cn.windery.sentinel.WindowWrap;
import cn.windery.sentinel.slots.degrade.DegradeRule;
import cn.windery.sentinel.slots.statistic.LeapArray;

import java.util.concurrent.atomic.LongAdder;

public class ResponseTimeCircuitBreaker extends AbstractCircuitBreaker {

    private final LeapArray<SlowRequestCounter> slowRequestLeapArray;

    public ResponseTimeCircuitBreaker(DegradeRule rule) {
        super(rule);
        slowRequestLeapArray = new SlowRequestLeapArray(rule.getSampleCount(), rule.getWindowLengthInMs());
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

        if (currentState() == State.HALF_OPEN) {
            if (rt > getThreshold()) {
                fromHalfOpenToOpen();
            } else {
                fromHalfOpenToClosed();
            }
            return;
        }

        System.out.println(counter.totalCount.longValue());
        if (counter.totalCount.longValue() < getMinRequests()) {
            return;
        }

        if (counter.slowCount.doubleValue() / counter.totalCount.doubleValue() > getThreshold()) {
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

    }

    static class SlowRequestLeapArray extends LeapArray<SlowRequestCounter> {

        public SlowRequestLeapArray(int sampleCount, int intervalInMs) {
            super(sampleCount, intervalInMs);
        }

        @Override
        public WindowWrap<SlowRequestCounter> newEmptyBucket(long windowStart, int windowLengthInMs) {
            return new WindowWrap<>(windowLengthInMs, windowStart, new SlowRequestCounter());
        }

        @Override
        protected void resetWindowTo(WindowWrap<SlowRequestCounter> windowWrap, long startTime) {
            windowWrap.resetTo(startTime);
            windowWrap.getValue().reset();
        }
    }

}

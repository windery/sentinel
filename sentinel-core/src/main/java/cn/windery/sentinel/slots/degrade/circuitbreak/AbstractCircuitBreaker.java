package cn.windery.sentinel.slots.degrade.circuitbreak;

import cn.windery.sentinel.Context;
import cn.windery.sentinel.ContextUtil;
import cn.windery.sentinel.slots.degrade.DegradeCallback;
import cn.windery.sentinel.slots.degrade.DegradeRule;
import cn.windery.sentinel.util.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractCircuitBreaker implements CircuitBreaker {

    private static final Logger log = LoggerFactory.getLogger(AbstractCircuitBreaker.class);

    protected final DegradeRule rule;
    protected volatile long nextRetryTimestamp = Long.MAX_VALUE;

    protected final AtomicReference<State> currentState = new AtomicReference<>(State.CLOSED);

    private final ReentrantLock RECOVER_LOCK = new ReentrantLock();

    protected final RecoverRequestCounter recoverRequestCounter;


    public AbstractCircuitBreaker(DegradeRule rule) {
        this.rule = rule;
        this.recoverRequestCounter = new RecoverRequestCounter();
    }

    @Override
    public boolean canPass() {
        if (currentState.get() == State.CLOSED) {
            return true;
        }

        if (currentState.get() == State.OPEN) {
            if (retryTimeArrived() && fromOpenToHalfOpen()) {
                if (recoverRequestCounter.getPass() < rule.getRecoverRequests()) {
                    if (passRecoverRequest()) {
                        return true;
                    }
                }
            }
            return false;
        }

        AssertUtil.isTrue(rule.getRecoverRequests() > 0, "recover pass must be positive");
        if (currentState.get() == State.HALF_OPEN && hasMoreRecoverRequests()) {
            if (passRecoverRequest()) {
                return true;
            }
        }

        return false;
    }

    private boolean hasMoreRecoverRequests() {
        return recoverRequestCounter.getPass() < rule.getRecoverRequests();
    }

    private boolean passRecoverRequest() {
        if (RECOVER_LOCK.tryLock()) {
            try {
                // double check
                if (hasMoreRecoverRequests()) {
                    recoverRequestCounter.addPass();
                    ContextUtil.getContext().setRecoverRequest(true);
                    log.debug("resource {} circuit breaker pass 1 recover request, current pass: {}", rule.getResource(), recoverRequestCounter.getPass());
                    return true;
                }
            } finally {
                RECOVER_LOCK.unlock();
            }
        }
        return false;
    }

    @Override
    public State currentState() {
        return currentState.get();
    }

    @Override
    public DegradeRule getRule() {
        return rule;
    }

    private boolean retryTimeArrived() {
        return System.currentTimeMillis() > nextRetryTimestamp;
    }

    protected boolean fromOpenToHalfOpen() {
        log.info("resource {} circuit breaker state change from OPEN to HALF_OPEN", rule.getResource());
        recoverRequestCounter.reset();
        boolean success = currentState.compareAndSet(State.OPEN, State.HALF_OPEN);
        if (success) {
            DegradeCallback callback = rule.getCallback();
            if (callback != null) {
                try {
                    Context context = ContextUtil.getContext();
                    String resource = context.getNode().getResource();
                    callback.fromOpenToHalfOpen(resource, statistic());
                } catch (Exception e) {
                    log.warn("DegradeCallback {} error: {}", rule.getResource(), e.getMessage());
                }
            }
        }
        return success;
    }

    protected boolean fromClosedToOpen() {
        log.info("resource {} circuit breaker state change from CLOSED to OPEN", rule.getResource());
        boolean success = currentState.compareAndSet(State.CLOSED, State.OPEN);
        if (success) {
            updateNextRetryTimestamp();
            DegradeCallback callback = rule.getCallback();
            if (callback != null) {
                try {
                    Context context = ContextUtil.getContext();
                    String resource = context.getNode().getResource();
                    callback.fromClosedToOpen(resource, statistic());
                } catch (Exception e) {
                    log.warn("DegradeCallback {} error: {}", rule.getResource(), e.getMessage());
                }
            }
        }
        return success;
    }

    protected boolean fromHalfOpenToOpen() {
        log.info("resource {} circuit breaker state change from HALF_OPEN to OPEN", rule.getResource());
        boolean success = currentState.compareAndSet(State.HALF_OPEN, State.OPEN);
        if (success) {
            updateNextRetryTimestamp();
            DegradeCallback callback = rule.getCallback();
            if (callback != null) {
                try {
                    Context context = ContextUtil.getContext();
                    String resource = context.getNode().getResource();
                    callback.fromHalfOpenToOpen(resource, statistic());
                } catch (Exception e) {
                    log.warn("DegradeCallback {} error: {}", rule.getResource(), e.getMessage());
                }
            }
        }
        return success;
    }

    protected boolean fromHalfOpenToClosed() {
        log.info("resource {} circuit breaker state change from HALF_OPEN to CLOSED", rule.getResource());
        boolean success = currentState.compareAndSet(State.HALF_OPEN, State.CLOSED);
        if (success) {
            DegradeCallback callback = rule.getCallback();
            if (callback != null) {
                try {
                    Context context = ContextUtil.getContext();
                    String resource = context.getNode().getResource();
                    callback.fromHalfOpenToClosed(resource, statistic());
                } catch (Exception e) {
                    log.warn("DegradeCallback {} error: {}", rule.getResource(), e.getMessage());
                }
            }
        }
        return success;
    }

    private void updateNextRetryTimestamp() {
        this.nextRetryTimestamp = System.currentTimeMillis() + rule.getRetryWaitTime();
    }


    protected double getThreshold() {
        return this.rule.getThreshold();
    }

    protected long getRetryWaitTime() {
        return this.rule.getRetryWaitTime();
    }

    protected long getMinRequests() {
        return this.rule.getMinRequests();
    }

    protected long getMaxRt() {
        return this.rule.getMaxRt();
    }


    static class RecoverRequestCounter {

        private LongAdder pass = new LongAdder();
        private LongAdder fail = new LongAdder();
        private LongAdder finished = new LongAdder();



        public void addPass() {
            pass.increment();
        }

        public void addFail() {
            fail.increment();
        }

        public void addFinished() {
            finished.increment();
        }

        public long getPass() {
            return pass.longValue();
        }

        public long getFail() {
            return fail.longValue();
        }

        public long getFinished() {
            return finished.longValue();
        }

        public void reset() {
            pass.reset();
            fail.reset();
            finished.reset();
        }

        @Override
        public String toString() {
            return "RecoverRequestCounter{" +
                    "pass=" + pass +
                    ", fail=" + fail +
                    ", finished=" + finished +
                    '}';
        }
    }

    public Object statistic() {
        return new Object();
    }

}

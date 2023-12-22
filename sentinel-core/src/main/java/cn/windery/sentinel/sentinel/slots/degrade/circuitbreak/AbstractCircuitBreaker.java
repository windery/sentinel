package cn.windery.sentinel.sentinel.slots.degrade.circuitbreak;

import cn.windery.sentinel.sentinel.slots.degrade.DegradeRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractCircuitBreaker implements CircuitBreaker {

    private static final Logger log = LoggerFactory.getLogger(AbstractCircuitBreaker.class);

    protected final DegradeRule rule;
    protected volatile long nextRetryTimestamp = Long.MAX_VALUE;

    protected final AtomicReference<State> currentState = new AtomicReference<>(State.CLOSED);


    public AbstractCircuitBreaker(DegradeRule rule) {
        this.rule = rule;
    }

    @Override
    public boolean canPass() {
        if (currentState.get() == State.CLOSED) {
            return true;
        }

        if (currentState.get() == State.OPEN) {
            if (retryTimeArrived() && fromOpenToHalfOpen()) {
                return true;
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
        log.debug("resource {} circuit breaker state change from OPEN to HALF_OPEN", rule.getResource());
        return currentState.compareAndSet(State.OPEN, State.HALF_OPEN);
    }

    protected boolean fromClosedToOpen() {
        log.debug("resource {} circuit breaker state change from CLOSED to OPEN", rule.getResource());
        updateNextRetryTimestamp();
        return currentState.compareAndSet(State.CLOSED, State.OPEN);
    }

    protected boolean fromHalfOpenToOpen() {
        log.debug("resource {} circuit breaker state change from HALF_OPEN to OPEN", rule.getResource());
        updateNextRetryTimestamp();
        return currentState.compareAndSet(State.HALF_OPEN, State.OPEN);
    }

    private void updateNextRetryTimestamp() {
        this.nextRetryTimestamp = System.currentTimeMillis() + rule.getRetryWaitTime();
    }

    protected boolean fromHalfOpenToClosed() {
        log.debug("resource {} circuit breaker state change from HALF_OPEN to CLOSED", rule.getResource());
        return currentState.compareAndSet(State.HALF_OPEN, State.CLOSED);
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

}

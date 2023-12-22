package cn.windery.sentinel.sentinel.slots.degrade.circuitbreak;

import cn.windery.learning.base.sentinel.Context;
import cn.windery.learning.base.sentinel.slots.degrade.DegradeRule;

public interface CircuitBreaker {

    DegradeRule getRule();

    boolean canPass();

    State currentState();

    void onRequestComplete(Context context);


    enum State {
        OPEN, CLOSED, HALF_OPEN
    }

}

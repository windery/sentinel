package cn.windery.sentinel;

import cn.windery.sentinel.slots.degrade.circuitbreak.CircuitBreakerManager;
import cn.windery.sentinel.slots.flow.FlowRaterManager;

public class AphInitializer {

    public static void initialize() {

        FlowRaterManager.initialize();
        CircuitBreakerManager.initialize();

    }


}

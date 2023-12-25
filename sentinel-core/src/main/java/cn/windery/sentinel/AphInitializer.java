package cn.windery.sentinel;

import cn.windery.sentinel.slots.Rule;
import cn.windery.sentinel.slots.degrade.circuitbreak.CircuitBreakerManager;
import cn.windery.sentinel.slots.flow.FlowRaterManager;
import cn.windery.sentinel.util.AssertUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AphInitializer {

    public static void initialize() {

        Map<String, List<Rule>> emptyRuleMap = new HashMap<>();

        initialize(emptyRuleMap);

    }

    public static void initialize(Map<String, List<Rule>> rulesMap) {

        FlowRaterManager.initialize();
        CircuitBreakerManager.initialize();


    }


}

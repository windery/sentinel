package cn.windery.sentinel.sentinel;

import cn.windery.sentinel.sentinel.slots.Rule;
import cn.windery.sentinel.sentinel.slots.degrade.circuitbreak.CircuitBreakerManager;
import cn.windery.sentinel.sentinel.slots.flow.FlowRaterManager;
import cn.windery.sentinel.sentinel.slots.flow.RuleManager;
import cn.windery.sentinel.sentinel.util.AssertUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AphInitializer {

    public static void initialize() {

        Map<String, List<Rule>> emptyRuleMap = new HashMap<>();

        initialize(emptyRuleMap);

    }

    public static void initialize(Map<String, List<Rule>> rulesMap) {

        AssertUtil.notNull(rulesMap, "rulesMap cannot be null");
        rulesMap.forEach(RuleManager::setRules);

        FlowRaterManager.initialize();
        CircuitBreakerManager.initialize();


    }


}

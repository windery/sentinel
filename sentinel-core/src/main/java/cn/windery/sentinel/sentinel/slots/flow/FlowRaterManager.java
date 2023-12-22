package cn.windery.sentinel.sentinel.slots.flow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FlowRaterManager {

    private static final Map<String, List<DefaultTrafficShapingController>> raterMap = new ConcurrentHashMap<>();

    public static void initialize() {

        List<FlowRule> flowRules = RuleManager.getAllFlowRules();
        // initialize flow rules
        for (FlowRule flowRule : flowRules) {
            List<DefaultTrafficShapingController> raters = raterMap.computeIfAbsent(flowRule.getResource(), resource -> new ArrayList<>());
            DefaultTrafficShapingController rater = new DefaultTrafficShapingController(flowRule.getThreshold());
            raters.add(rater);
        }

    }

    public static List<DefaultTrafficShapingController> getRaters(String resource) {
        return raterMap.get(resource);
    }

}

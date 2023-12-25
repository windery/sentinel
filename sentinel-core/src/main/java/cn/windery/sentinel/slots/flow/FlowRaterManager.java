package cn.windery.sentinel.slots.flow;

import cn.windery.sentinel.slots.FlowRuleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FlowRaterManager {

    private static final Logger log = LoggerFactory.getLogger(FlowRaterManager.class);
    private static final Map<String, List<DefaultFlowRater>> raterMap = new ConcurrentHashMap<>();

    public static void initialize() {

        Map<String, List<FlowRule>> flowRules = FlowRuleManager.getInstance().getRules();

        flowRules.forEach((resource, rules) -> {
            // initialize flow rater
            for (FlowRule rule : rules) {
                try {
                    List<DefaultFlowRater> raters = raterMap.computeIfAbsent(rule.getResource(), k -> new ArrayList<>());
                    DefaultFlowRater flowRater = new DefaultFlowRater(rule.getThreshold());
                    raters.add(flowRater);
                } catch (Exception e) {
                     log.warn("Failed to initialize flow rater for resource {}", rule.getResource(), e);
                }
            }
        });

    }

    public static List<DefaultFlowRater> getRaters(String resource) {
        return raterMap.get(resource);
    }

}

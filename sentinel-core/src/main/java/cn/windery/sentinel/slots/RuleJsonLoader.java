package cn.windery.sentinel.slots;

import cn.windery.sentinel.slots.degrade.DegradeRule;
import cn.windery.sentinel.slots.degrade.DegradeRuleManager;
import cn.windery.sentinel.slots.flow.FlowRule;
import cn.windery.sentinel.util.AssertUtil;
import cn.windery.sentinel.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleJsonLoader implements RuleLoader {

    private static final Logger log = LoggerFactory.getLogger(RuleJsonLoader.class);


    @Override
    public void load(String source) {

        try {
            Map<String, List<FlowRule>> flowRuleMap = new HashMap<>();
            Map<String, List<DegradeRule>> degradeRuleMap = new HashMap<>();

            JsonNode jsonNode = JsonUtil.readTree(source);
            JsonNode flowRulesNode = jsonNode.get("flow_rules");
            if (flowRulesNode != null) {
                flowRulesNode.fieldNames().forEachRemaining(resource -> {
                    try {
                        JsonNode resourceNode = flowRulesNode.get(resource);
                        for (JsonNode flowRuleNode : resourceNode) {
                            int threshold = flowRuleNode.path("threshold").asInt();
                            FlowRule flowRule = new FlowRule(resource, threshold);
                            flowRuleMap.computeIfAbsent(resource, k -> new ArrayList<>()).add(flowRule);
                        }
                    } catch (Exception e) {
                        log.error("Failed to load resource {} flow rules", resource, e);
                    }
                });
                FlowRuleManager.getInstance().setRulesMap(flowRuleMap);
            }

            // degrade rules
            JsonNode degradeRulesNode = jsonNode.get("degrade_rules");
            if (degradeRulesNode != null) {
                degradeRulesNode.fieldNames().forEachRemaining(resource -> {
                    try {
                        JsonNode resourceNode = degradeRulesNode.get(resource);
                        DegradeRule degradeRule = new DegradeRule(resource);
                        for (JsonNode degradeRuleNode : resourceNode) {
                            JsonNode threshold = degradeRuleNode.path("threshold");
                            AssertUtil.notNull(threshold, "degrade rule threshold cannot be null");
                            double thresholdVal = threshold.asDouble();
                            AssertUtil.isTrue(thresholdVal > 0 && thresholdVal <= 1.0, "degrade rule threshold should be (0, 1.0]");
                            degradeRule.setThreshold(thresholdVal);
                            degradeRule.setRetryWaitTime(degradeRuleNode.path("retryWaitTime").asLong(1000));
                            degradeRule.setMinRequests(degradeRuleNode.path("minRequests").asInt(10));
                            degradeRule.setMaxRt(degradeRuleNode.path("maxRt").asInt(1000));
                            degradeRule.setWindowLengthInMs(degradeRuleNode.path("windowLengthInMs").asInt(1000));
                            degradeRuleMap.computeIfAbsent(resource, k -> new ArrayList<>()).add(degradeRule);
                        }
                    } catch (Exception e) {
                        log.error("Failed to load resource {} degrade rules", resource, e);
                    }
                });
                DegradeRuleManager.getInstance().setRulesMap(degradeRuleMap);
            }
        } catch (Exception e) {
            log.error("Failed to load rules from json data", e);
        }

    }
}

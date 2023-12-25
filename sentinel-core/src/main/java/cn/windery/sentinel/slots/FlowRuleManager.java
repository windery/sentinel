package cn.windery.sentinel.slots;

import cn.windery.sentinel.slots.flow.FlowRule;


public class FlowRuleManager extends RuleManager<FlowRule> {

    private static final FlowRuleManager INSTANCE = new FlowRuleManager();

    private FlowRuleManager() {}

    public static FlowRuleManager getInstance() {
        return INSTANCE;
    }

}

package cn.windery.sentinel.slots.degrade;

import cn.windery.sentinel.slots.RuleManager;

public class DegradeRuleManager extends RuleManager<DegradeRule> {

    private static final DegradeRuleManager INSTANCE = new DegradeRuleManager();

    private DegradeRuleManager() {}

    public static DegradeRuleManager getInstance() {
        return INSTANCE;
    }

}

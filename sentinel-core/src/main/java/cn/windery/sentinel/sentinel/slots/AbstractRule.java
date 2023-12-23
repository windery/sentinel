package cn.windery.sentinel.sentinel.slots;

import cn.windery.sentinel.sentinel.RuleType;

public abstract class AbstractRule implements Rule {

    protected String resource;
    protected RuleType ruleType;

    public AbstractRule(String resource, RuleType ruleType) {
        this.resource = resource;
        this.ruleType = ruleType;
    }

    @Override
    public String getResource() {
        return resource;
    }

    @Override
    public RuleType getType() {
        return ruleType;
    }
}

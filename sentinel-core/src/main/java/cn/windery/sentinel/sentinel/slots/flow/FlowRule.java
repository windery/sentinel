package cn.windery.sentinel.sentinel.slots.flow;

import cn.windery.sentinel.sentinel.RuleType;
import cn.windery.sentinel.sentinel.slots.AbstractRule;

/**
 * 限流规则
 */
public class FlowRule extends AbstractRule {

    private final int threshold;


    public FlowRule(String resource, int threshold) {
        super(resource, RuleType.FLOW);
        this.threshold = threshold;
    }


    public int getThreshold() {
        return threshold;
    }

}

package cn.windery.sentinel.slots.flow;

import cn.windery.sentinel.RuleType;
import cn.windery.sentinel.slots.AbstractRule;

import java.util.Objects;

/**
 * 限流规则
 */
public class FlowRule extends AbstractRule {

    private int threshold;

    public FlowRule(String resource, int threshold) {
        super(resource, RuleType.FLOW.name().toLowerCase());
        this.threshold = threshold;
    }


    public int getThreshold() {
        return threshold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FlowRule)) return false;
        FlowRule flowRule = (FlowRule) o;
        return threshold == flowRule.threshold;
    }

    @Override
    public int hashCode() {
        return Objects.hash(threshold);
    }

    @Override
    public String toString() {
        return "FlowRule{" +
                "threshold=" + threshold +
                ", resource='" + resource + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}

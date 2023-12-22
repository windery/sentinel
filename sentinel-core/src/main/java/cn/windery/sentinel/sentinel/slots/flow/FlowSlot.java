package cn.windery.sentinel.sentinel.slots.flow;

import cn.windery.learning.base.sentinel.Context;
import cn.windery.learning.base.sentinel.exception.BlockException;
import cn.windery.learning.base.sentinel.slots.AbstractLinkedProcessSlot;
import cn.windery.learning.base.sentinel.slots.StatisticNode;

public class FlowSlot extends AbstractLinkedProcessSlot<StatisticNode> {

    private final FlowRuleChecker checker;

    public FlowSlot() {
        this.checker = new FlowRuleChecker();
    }

    @Override
    public void entry(Context context, String resource) {
        StatisticNode node = context.getNode();
        boolean canPass = checker.canPass(node);
        if (!canPass) {
            throw new BlockException("resource " + resource + " blocked by FlowSlot");
        }
        fireEntry(context, resource);
    }

    @Override
    public void exit(Context context, String resource) {
        fireExit(context, resource);
    }

}

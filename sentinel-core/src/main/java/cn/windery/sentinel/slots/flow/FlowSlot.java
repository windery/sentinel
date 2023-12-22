package cn.windery.sentinel.slots.flow;

import cn.windery.sentinel.Context;
import cn.windery.sentinel.exception.BlockException;
import cn.windery.sentinel.slots.AbstractLinkedProcessSlot;
import cn.windery.sentinel.slots.StatisticNode;

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

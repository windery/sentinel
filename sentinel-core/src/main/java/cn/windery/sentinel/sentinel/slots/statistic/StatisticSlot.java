package cn.windery.sentinel.sentinel.slots.statistic;

import cn.windery.learning.base.sentinel.Context;
import cn.windery.learning.base.sentinel.exception.BlockException;
import cn.windery.learning.base.sentinel.slots.AbstractLinkedProcessSlot;
import cn.windery.learning.base.sentinel.slots.StatisticNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StatisticSlot extends AbstractLinkedProcessSlot<StatisticNode> {

    @Override
    public void entry(Context context, String resource) {
        context.setEntryTime(System.currentTimeMillis());
        StatisticNode node = context.getNode();
        try {
            fireEntry(context, resource);
            node.increase(MetricEvent.PASS);
        } catch (BlockException blockException) {
            // increase block qps
            node.increase(MetricEvent.BLOCK);
            throw blockException;
        } catch (Throwable e) {
            throw e;
        }
    }

    @Override
    public void exit(Context context, String resource) {
        StatisticNode node = context.getNode();
        node.increase(MetricEvent.SUCCESS);
        node.add(MetricEvent.RT, (int)(System.currentTimeMillis()- context.getEntryTime()));
        if (context.getBlockError() == null && context.getError() != null) {
            node.increase(MetricEvent.EXCEPTION);
        }
        fireExit(context, resource);
    }

}

package cn.windery.sentinel.slots.nodeselect;

import cn.windery.sentinel.Context;
import cn.windery.sentinel.slots.AbstractLinkedProcessSlot;
import cn.windery.sentinel.slots.StatisticNode;
import cn.windery.sentinel.util.AssertUtil;

import java.util.HashMap;
import java.util.Map;

public class NodeSelectSlot extends AbstractLinkedProcessSlot<StatisticNode> {

    private static volatile Map<String, StatisticNode> nodeMap = new HashMap<>();

    @Override
    public void entry(Context context, String resource) {
        AssertUtil.notNull(context, "context cannot be null");

        StatisticNode node = nodeMap.get(resource);
        if (node == null) {
            node = new StatisticNode(resource);
            Map<String, StatisticNode> newMap = new HashMap<>(nodeMap);
            newMap.put(resource, node);
            nodeMap = newMap;
        }
        context.setNode(node);
        fireEntry(context, resource);
    }

    @Override
    public void exit(Context context, String resource) {
        fireExit(context, resource);
    }

    public StatisticNode getNode(String resource) {
        return nodeMap.get(resource);
    }

}

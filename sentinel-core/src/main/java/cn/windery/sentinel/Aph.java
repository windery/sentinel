package cn.windery.sentinel;

import cn.windery.sentinel.slots.*;
import cn.windery.sentinel.slots.degrade.DegradeSlot;
import cn.windery.sentinel.slots.degrade.circuitbreak.AbstractCircuitBreaker;
import cn.windery.sentinel.slots.degrade.circuitbreak.CircuitBreaker;
import cn.windery.sentinel.slots.degrade.circuitbreak.CircuitBreakerManager;
import cn.windery.sentinel.slots.nodeselect.NodeSelectSlot;
import cn.windery.sentinel.slots.statistic.ArrayMetric;
import cn.windery.sentinel.slots.statistic.Metric;
import cn.windery.sentinel.util.AssertUtil;

import java.util.*;

public class Aph {

    // 资源 -> logicChain
    private static volatile Map<String, ProcessorSlotChain> chainMap = new HashMap<>();

    private static final Object lock = new Object();

    public static void entry(String resource) {

        ProcessorSlotChain chain = chainMap.get(resource);
        if (chain == null) {
            synchronized (lock) {
                chain = chainMap.get(resource);
                if (chain == null) {
                    chain = SlotChainBuilder.build();
                    chainMap.put(resource, chain);
                }
            }
        }

        Context context = ContextUtil.getContext();
        context.setEntryTime(System.currentTimeMillis());
        chain.entry(context, resource);
    }

    public static void exit(String resource) {
        ProcessorSlotChain chain = chainMap.get(resource);
        AssertUtil.notNull(chain, "chain cannot be null");
        Context context = ContextUtil.getContext();
        chain.exit(context, resource);
        ContextUtil.clearContext();
    }

    public static List<String> resources() {
        return new ArrayList<>(chainMap.keySet());
    }

    public static Map<Long, Metric> minuteMetrics(String resource) {
        ProcessorSlotChain chain = chainMap.get(resource);
        if (chain != null && chain instanceof DefaultProcessorSlotChain) {
            AbstractLinkedProcessSlot slot = ((DefaultProcessorSlotChain) chain).getFirst();
            while ((slot = slot.getNext()) != null) {
                if (slot instanceof NodeSelectSlot) {
                    StatisticNode node = ((NodeSelectSlot) slot).getNode(resource);
                    if (node != null) {
                        ArrayMetric arrayMetrics = node.getMinuteMtrics();
                        if (arrayMetrics != null) {
                            Map<Long, Metric> values = arrayMetrics.valueMap(System.currentTimeMillis());
                            if (values == null) {
                                return Collections.emptyMap();
                            }
                            return values;
                        }
                    }
                }
            }
        }
        return Collections.emptyMap();
    }

}

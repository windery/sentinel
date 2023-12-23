package cn.windery.sentinel.sentinel.slots;

import cn.windery.sentinel.sentinel.log.LogSlot;
import cn.windery.sentinel.sentinel.slots.degrade.DegradeSlot;
import cn.windery.sentinel.sentinel.slots.flow.FlowSlot;
import cn.windery.sentinel.sentinel.slots.nodeselect.NodeSelectSlot;
import cn.windery.sentinel.sentinel.slots.statistic.StatisticSlot;

public class SlotChainBuilder {

    public static ProcessorSlotChain build() {
        ProcessorSlotChain chain = new DefaultProcessorSlotChain();
        chain.addLast(new NodeSelectSlot());
        chain.addLast(new LogSlot());
        chain.addLast(new StatisticSlot());
        chain.addLast(new FlowSlot());
        chain.addLast(new DegradeSlot());
        return chain;
    }

}

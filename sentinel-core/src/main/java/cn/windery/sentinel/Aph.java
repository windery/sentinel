package cn.windery.sentinel;

import cn.windery.sentinel.slots.ProcessorSlotChain;
import cn.windery.sentinel.slots.SlotChainBuilder;
import cn.windery.sentinel.util.AssertUtil;

import java.util.HashMap;
import java.util.Map;

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
        chain.entry(context, resource);
    }

    public static void exit(String resource) {
        ProcessorSlotChain chain = chainMap.get(resource);
        AssertUtil.notNull(chain, "chain cannot be null");
        Context context = ContextUtil.getContext();
        chain.exit(context, resource);
        ContextUtil.clearContext();
    }

}

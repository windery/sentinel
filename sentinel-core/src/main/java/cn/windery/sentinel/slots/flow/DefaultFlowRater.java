package cn.windery.sentinel.slots.flow;

import cn.windery.sentinel.slots.StatisticNode;

public class DefaultFlowRater implements FlowRater {

    private final int threshold;

    public DefaultFlowRater(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean canPass(StatisticNode node) {
        long pass = node.pass();
        return pass + 1 <= threshold;
    }


}

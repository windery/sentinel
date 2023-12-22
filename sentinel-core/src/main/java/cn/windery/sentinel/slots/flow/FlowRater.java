package cn.windery.sentinel.slots.flow;

import cn.windery.sentinel.slots.StatisticNode;

public interface FlowRater {

    boolean canPass(StatisticNode node);

}

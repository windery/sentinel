package cn.windery.sentinel.sentinel.slots.flow;

import cn.windery.learning.base.sentinel.slots.StatisticNode;

public interface TrafficShapingController {

    boolean canPass(StatisticNode node);

}

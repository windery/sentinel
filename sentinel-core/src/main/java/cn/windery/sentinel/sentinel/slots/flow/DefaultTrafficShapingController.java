package cn.windery.sentinel.sentinel.slots.flow;

import cn.windery.learning.base.sentinel.slots.StatisticNode;

public class DefaultTrafficShapingController implements TrafficShapingController {

    private final int threshold;

    public DefaultTrafficShapingController(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean canPass(StatisticNode node) {
        long pass = node.pass();
        return pass + 1 <= threshold;
    }


}

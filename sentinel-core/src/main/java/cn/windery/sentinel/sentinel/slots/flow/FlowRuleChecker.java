package cn.windery.sentinel.sentinel.slots.flow;

import cn.windery.learning.base.sentinel.slots.StatisticNode;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

public class FlowRuleChecker {

    public boolean canPass(StatisticNode node) {
        String resource = node.getResource();
        List<DefaultTrafficShapingController> raters = FlowRaterManager.getRaters(resource);
        if (!CollectionUtils.isEmpty(raters)) {
            for (DefaultTrafficShapingController rater : raters) {
                if (!rater.canPass(node)) {
                    return false;
                }
            }
        }
        return true;
    }

}

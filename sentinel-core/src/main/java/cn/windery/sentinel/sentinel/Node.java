package cn.windery.sentinel.sentinel;

import cn.windery.learning.base.sentinel.slots.statistic.MetricEvent;

public interface Node {
    void add(MetricEvent event, int n);
    void increase(MetricEvent event);


}

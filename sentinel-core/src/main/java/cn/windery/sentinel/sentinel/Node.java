package cn.windery.sentinel.sentinel;

import cn.windery.sentinel.sentinel.slots.statistic.MetricEvent;

public interface Node {
    void add(MetricEvent event, int n);
    void increase(MetricEvent event);


}

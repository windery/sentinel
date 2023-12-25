package cn.windery.sentinel;

import cn.windery.sentinel.slots.statistic.MetricEvent;

public interface Node {
    void add(MetricEvent event, int n);
    void increase(MetricEvent event);


}

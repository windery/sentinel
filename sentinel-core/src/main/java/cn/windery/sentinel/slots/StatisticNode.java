package cn.windery.sentinel.slots;

import cn.windery.sentinel.ContextUtil;
import cn.windery.sentinel.Node;
import cn.windery.sentinel.slots.statistic.ArrayMetric;
import cn.windery.sentinel.slots.statistic.MetricEvent;
import cn.windery.sentinel.slots.statistic.SecondMetric;

public class StatisticNode implements Node, SecondMetric {

    private String resource;

    // 1 second -> 2 500ms buckets
    private ArrayMetric secondMtrics = new ArrayMetric(2, 1000);
    // 1 minute -> 60 1s buckets
    private ArrayMetric minuteMtrics = new ArrayMetric(60, 60*1000);

    public StatisticNode(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }

    public void add(MetricEvent event, int n) {
        long entryTime = ContextUtil.getContext().getEntryTime();
        secondMtrics.currentWindow(entryTime).getValue().add(event, n);
        minuteMtrics.currentWindow(entryTime).getValue().add(event, n);
    }

    @Override
    public void increase(MetricEvent event) {
        long entryTime = ContextUtil.getContext().getEntryTime();
        secondMtrics.currentWindow(entryTime).getValue().increment(event);
        minuteMtrics.currentWindow(entryTime).getValue().increment(event);
    }

    public ArrayMetric getMinuteMtrics() {
        return minuteMtrics;
    }


    @Override
    public long pass() {
        return secondMtrics.pass();
    }

    @Override
    public long block() {
        return secondMtrics.block();
    }

    @Override
    public long exception() {
        return secondMtrics.exception();
    }

    @Override
    public long success() {
        return secondMtrics.success();
    }

    @Override
    public long rt() {
        return secondMtrics.rt();
    }

}

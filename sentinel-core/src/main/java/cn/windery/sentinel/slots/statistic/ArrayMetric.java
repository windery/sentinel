package cn.windery.sentinel.slots.statistic;

import cn.windery.sentinel.WindowWrap;

import java.util.List;

public class ArrayMetric extends LeapArray<Metric> implements SecondMetric {

    public ArrayMetric(int sampleCount, int intervalInMs) {
        super(sampleCount, intervalInMs);
    }

    @Override
    public WindowWrap<Metric> newEmptyBucket(long windowStart, int windowLengthInMs) {
        return new WindowWrap<Metric>(windowStart, windowLengthInMs, new Metric());
    }


    @Override
    protected void resetWindowTo(WindowWrap<Metric> windowWrap, long windowStart) {
        windowWrap.resetTo(windowStart);
        windowWrap.getValue().reset();
    }

    @Override
    public long pass() {
        MetricEvent event = MetricEvent.PASS;
        return eventWindowSum(event);
    }

    @Override
    public long block() {
        return eventWindowSum(MetricEvent.BLOCK);
    }

    @Override
    public long exception() {
        return eventWindowSum(MetricEvent.EXCEPTION);
    }

    @Override
    public long success() {
        return eventWindowSum(MetricEvent.SUCCESS);
    }

    @Override
    public long rt() {
        long passQps = eventWindowSum(MetricEvent.PASS);
        if (passQps == 0) {
            return 0;
        }
        long rt = eventWindowSum(MetricEvent.RT);
        return rt / passQps;
    }

    private long eventWindowSum(MetricEvent event) {
        long metric = 0;
        List<Metric> values = values(System.currentTimeMillis());
        for (Metric value : values) {
            metric += value.get(event);
        }
        return metric;
    }

}

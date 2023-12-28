package cn.windery.sentinel.slots.statistic;

import java.util.concurrent.atomic.LongAdder;

public class Metric {

    private LongAdder[] counters = new LongAdder[MetricEvent.values().length];

    public Metric() {
        for (MetricEvent event : MetricEvent.values()) {
            counters[event.ordinal()] = new LongAdder();
        }
    }

    public void reset() {
        for (MetricEvent event : MetricEvent.values()) {
            counters[event.ordinal()].reset();
        }
    }

    public void add(MetricEvent event, int n) {
        counters[event.ordinal()].add(n);
    }

    public void increment(MetricEvent event) {
        add(event, 1);
    }

    public long get(MetricEvent event) {
        return counters[event.ordinal()].longValue();
    }


}

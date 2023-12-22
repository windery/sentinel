package cn.windery.sentinel.slots.statistic;

public class MetricSnapshot {

    private long timestamp;
    private long[] counters = new long[MetricEvent.values().length];

    public MetricSnapshot(long timestamp) {
        this.timestamp = timestamp;
        for (MetricEvent event : MetricEvent.values()) {
            counters[event.ordinal()] = 0;
        }
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long get(MetricEvent event) {
        return counters[event.ordinal()];
    }

    public void set(MetricEvent event, long n) {
        counters[event.ordinal()] = n;
    }


}

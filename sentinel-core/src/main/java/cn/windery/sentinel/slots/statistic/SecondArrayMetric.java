package cn.windery.sentinel.slots.statistic;

import cn.windery.sentinel.Context;
import cn.windery.sentinel.ContextUtil;
import cn.windery.sentinel.WindowWrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SecondArrayMetric extends ArrayMetric {

    private static final Logger metricLogger = LoggerFactory.getLogger("second-metric");
    private static List<MetricEvent> logEvents = Arrays.asList(MetricEvent.PASS, MetricEvent.BLOCK);

    public SecondArrayMetric() {
        super(2, 1000);
    }

    @Override
    protected void logWindowStatistic(WindowWrap<Metric> window) {
        Metric metric = window.getValue();
        long windowStart = window.getWindowStart();
        if (windowStart <= 0) {
            return;
        }
        MetricSnapshot snapshot = new MetricSnapshot(windowStart);
        for (MetricEvent event : MetricEvent.values()) {
            snapshot.set(event, metric.get(event));
        }

        String logContent = buildLogContent(snapshot);

        metricLogger.info(logContent);

    }

    private String buildLogContent(MetricSnapshot snapshot) {

        Context context = ContextUtil.getContext();
        String resource = context.getNode().getResource();
        StringBuilder content = new StringBuilder();
        content.append(resource).append("|");
        content.append(snapshot.getTimestamp()).append("|");
        String metricContent = logEvents.stream().map(event -> String.valueOf(snapshot.get(event))).collect(Collectors.joining("|"));
        content.append(metricContent);

        return content.toString();
    }

}

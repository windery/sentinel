package cn.windery.sentinel.log;

import cn.windery.sentinel.Context;
import cn.windery.sentinel.slots.AbstractLinkedProcessSlot;
import cn.windery.sentinel.slots.StatisticNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogSlot extends AbstractLinkedProcessSlot<StatisticNode> {

    private static final Logger log = LoggerFactory.getLogger(LogSlot.class);


    @Override
    public void entry(Context context, String resource) {
        fireEntry(context, resource);
        StatisticNode node = context.getNode();
        log.info("LogSlot entry: \n{}", logStatistic(node));
    }

    @Override
    public void exit(Context context, String resource) {
        fireExit(context, resource);
        StatisticNode node = context.getNode();
        log.info("LogSlot exit: \n{}", logStatistic(node));
    }

    public String logStatistic(StatisticNode node) {
        StringBuilder content = new StringBuilder();
        content.append("statistic: ");
        StringBuilder minuteContent = new StringBuilder();
        content.append("passQps=[").append(node.pass()).append("] , ");
        content.append("blockQps=[").append(node.block()).append("] , ");
        content.append("exceptionQps=[").append(node.exception()).append("] , ");
        content.append("successQps=[").append(node.success()).append("] , ");
        content.append("rt=[").append(node.rt()).append("] , ");
        return content + "\n" + minuteContent;
    }

}

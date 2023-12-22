package cn.windery.sentinel.sentinel.log;

import cn.windery.learning.base.sentinel.Context;
import cn.windery.learning.base.sentinel.slots.AbstractLinkedProcessSlot;
import cn.windery.learning.base.sentinel.slots.StatisticNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogSlot extends AbstractLinkedProcessSlot<StatisticNode> {


    @Override
    public void entry(Context context, String resource) {
        fireEntry(context, resource);
        StatisticNode node = context.getNode();
//        log.info("LogSlot entry: \n{}", logStatistic(node));
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

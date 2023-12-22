package cn.windery.sentinel.sentinel.slots.degrade;

import cn.windery.sentinel.sentinel.Context;
import cn.windery.sentinel.sentinel.exception.DegradeException;
import cn.windery.sentinel.sentinel.slots.AbstractLinkedProcessSlot;
import cn.windery.sentinel.sentinel.slots.StatisticNode;
import cn.windery.sentinel.sentinel.slots.degrade.circuitbreak.CircuitBreaker;
import cn.windery.sentinel.sentinel.slots.degrade.circuitbreak.CircuitBreakerManager;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

public class DegradeSlot extends AbstractLinkedProcessSlot<StatisticNode> {

    @Override
    public void entry(Context context, String resource) {

        List<CircuitBreaker> breakers = CircuitBreakerManager.getBreakers(resource);
        if (!CollectionUtils.isEmpty(breakers)) {
            for (CircuitBreaker breaker : breakers) {
                if (!breaker.canPass()) {
                    throw new DegradeException("resource " + resource + " blocked by DegradeSlot");
                }
            }
        }

        fireEntry(context, resource);
    }

    @Override
    public void exit(Context context, String resource) {
        List<CircuitBreaker> breakers = CircuitBreakerManager.getBreakers(resource);
        if (!CollectionUtils.isEmpty(breakers)) {
            for (CircuitBreaker breaker : breakers) {
                if (context.getBlockError() == null) {
                    breaker.onRequestComplete(context);
                }
            }
        }
        fireExit(context, resource);
    }

}

package cn.windery.sentinel.slots.degrade;

import cn.windery.sentinel.Context;
import cn.windery.sentinel.exception.DegradeException;
import cn.windery.sentinel.slots.AbstractLinkedProcessSlot;
import cn.windery.sentinel.slots.StatisticNode;
import cn.windery.sentinel.slots.degrade.circuitbreak.CircuitBreaker;
import cn.windery.sentinel.slots.degrade.circuitbreak.CircuitBreakerManager;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class DegradeSlot extends AbstractLinkedProcessSlot<StatisticNode> {

    @Override
    public void entry(Context context, String resource) {

        List<CircuitBreaker> breakers = CircuitBreakerManager.getBreakers(resource);
        if (breakers == null) {
            synchronized (this) {
                if (breakers == null) {
                    breakers = CircuitBreakerManager.createDefaultBreakersFromRules(resource);
                }
            }
        }
        if (!CollectionUtils.isEmpty(breakers)) {
            for (CircuitBreaker breaker : breakers) {
                if (!breaker.canPass()) {
                    throw new DegradeException("resource " + resource + " blocked by DegradeSlot", breaker.getRule());
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

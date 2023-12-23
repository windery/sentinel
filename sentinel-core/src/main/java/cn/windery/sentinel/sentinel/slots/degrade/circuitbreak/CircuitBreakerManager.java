package cn.windery.sentinel.sentinel.slots.degrade.circuitbreak;

import cn.windery.sentinel.sentinel.slots.degrade.DegradeRule;
import cn.windery.sentinel.sentinel.slots.flow.RuleManager;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CircuitBreakerManager {

    private static final Map<String, List<CircuitBreaker>> breakerMap = new ConcurrentHashMap<>();

    public static void initialize() {

        List<DegradeRule> degradeRules = RuleManager.getDegradeRules();
        if (CollectionUtils.isEmpty(degradeRules)) {
            return;
        }

        for (DegradeRule degradeRule : degradeRules) {
            CircuitBreaker circuitBreaker = new ResponseTimeCircuitBreaker(degradeRule);
            List<CircuitBreaker> breakers = breakerMap.computeIfAbsent(degradeRule.getResource(), resource -> new ArrayList<>());
            breakers.add(circuitBreaker);
        }

    }

    public static List<CircuitBreaker> getBreakers(String resource) {
        return breakerMap.get(resource);
    }

    public static void addBreaker(String resource, CircuitBreaker circuitBreaker) {
        List<CircuitBreaker> breakers = breakerMap.get(resource);
        List<CircuitBreaker> newBreakers = new ArrayList<>();
        if (!CollectionUtils.isEmpty(breakers)) {
            newBreakers.addAll(breakers);
        }
        newBreakers.add(circuitBreaker);
        breakerMap.put(resource, newBreakers);
    }

}

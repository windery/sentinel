package cn.windery.sentinel.slots.degrade.circuitbreak;

import cn.windery.sentinel.slots.degrade.DegradeRule;
import cn.windery.sentinel.slots.degrade.DegradeRuleManager;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CircuitBreakerManager {

    private static final Logger log = LoggerFactory.getLogger(CircuitBreakerManager.class);

    private static final Map<String, List<CircuitBreaker>> breakerMap = new ConcurrentHashMap<>();

    public static void initialize() {

        Map<String, List<DegradeRule>> degradeRules = DegradeRuleManager.getInstance().getRules();

        degradeRules.forEach((resource, rules) -> {
            // initialize circuit breaker
            for (DegradeRule rule : rules) {
                try {
                    CircuitBreaker circuitBreaker = new ResponseTimeCircuitBreaker(rule);
                    List<CircuitBreaker> breakers = breakerMap.computeIfAbsent(rule.getResource(), k -> new ArrayList<>());
                    breakers.add(circuitBreaker);
                } catch (Exception e) {
                    log.warn("Failed to initialize circuit breaker for resource {}", rule.getResource(), e);
                }
            }
        });

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

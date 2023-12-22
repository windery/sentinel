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
        List<CircuitBreaker> circuitBreakers = breakerMap.get(resource);
        return circuitBreakers;
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

    // 这个操作比较轻量级，直接加synchronized锁
    public static List<CircuitBreaker> createDefaultBreakersFromRules(String resource) {

        List<DegradeRule> degradeRules = DegradeRuleManager.getInstance().defaultRules();
        if (degradeRules != null) {
            List<CircuitBreaker> breakers = new ArrayList<>();
            for (DegradeRule degradeRule : degradeRules) {
                try {
                    CircuitBreaker circuitBreaker = new ResponseTimeCircuitBreaker(degradeRule);
                    breakers.add(circuitBreaker);
                } catch (Exception e) {
                    log.warn("Failed to initialize circuit breaker for resource {}", degradeRule.getResource(), e);
                }
            }
            breakerMap.put(resource, breakers);
        }

        return breakerMap.get(resource);
    }

}

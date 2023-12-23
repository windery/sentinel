package cn.windery.sentinel.sentinel.slots.flow;

import cn.windery.sentinel.sentinel.RuleType;
import cn.windery.sentinel.sentinel.slots.Rule;
import cn.windery.sentinel.sentinel.slots.degrade.DegradeRule;
import cn.windery.sentinel.sentinel.util.AssertUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class RuleManager {

    private static volatile Map<String, List<Rule>> rulesMap = new ConcurrentHashMap<>();


    public static List<Rule> getAllRules() {
        List<Rule> rules = new ArrayList<>();
        for (List<Rule> ruleList : rulesMap.values()) {
            if (!CollectionUtils.isEmpty(ruleList)) {
                rules.addAll(ruleList);
            }
        }
        return rules;
    }


    public static List<Rule> getRules(String resource) {
        AssertUtil.notNull(rulesMap, "RuleManager has not been initialized");
        return rulesMap.get(resource);
    }

    public static List<FlowRule> getAllFlowRules() {
        List<Rule> rules = getAllRules();
        if (CollectionUtils.isEmpty(rules)) {
            return new ArrayList<>();
        }
        return rules.stream().filter(rule -> rule.getType() == RuleType.FLOW).map(rule -> (FlowRule) rule).collect(Collectors.toList());
    }

    public static List<FlowRule> getFlowRules(String resource) {
        List<Rule> rules = getRules(resource);
        if (rules == null) {
            return new ArrayList<>();
        }
        return rules.stream().filter(rule -> rule.getType() == RuleType.FLOW).map(rule -> (FlowRule) rule).collect(Collectors.toList());
    }

    public static List<DegradeRule> getDegradeRules() {
        List<Rule> rules = getAllRules();
        if (CollectionUtils.isEmpty(rules)) {
            return new ArrayList<>();
        }
        return rules.stream().filter(rule -> rule.getType() == RuleType.DEGRADE).map(rule -> (DegradeRule) rule).collect(Collectors.toList());
    }

    public static List<DegradeRule> getDegradeRules(String resource) {
        List<Rule> rules = getRules(resource);
        if (rules == null) {
            return new ArrayList<>();
        }
        return rules.stream().filter(rule -> rule.getType() == RuleType.DEGRADE).map(rule -> (DegradeRule) rule).collect(Collectors.toList());
    }

    public static void setRules(String resource, List<Rule> rules) {
        AssertUtil.notNull(rulesMap, "rules null");
        rulesMap.put(resource, rules);
    }

    public static void addRule(String resource, Rule rule) {
        AssertUtil.notNull(rulesMap, "RuleManager has not been initialized");
        List<Rule> rules = rulesMap.computeIfAbsent(resource, k -> new ArrayList<>());
        ArrayList<Rule> newRules = new ArrayList<>(rules);
        newRules.add(rule);
        rulesMap.put(resource, newRules);
    }


}

package cn.windery.sentinel.slots;

import cn.windery.sentinel.util.AssertUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RuleManager<R extends Rule> {

    private volatile Map<String, List<R>> rulesMap = new ConcurrentHashMap<>();

    public Map<String, List<R>> getRules() {
        return rulesMap;
    }

    public List<R> defaultRules() {
        List<R> defaultRules = rulesMap.get("default");
        if (defaultRules == null) {
            return Collections.emptyList();
        }
        return defaultRules;
    }

    public List<R> getRules(String resource) {
        List<R> flowRules = rulesMap.get(resource);
        if (CollectionUtils.isEmpty(flowRules)) {
            return Collections.emptyList();
        }

        return flowRules;
    }

    public void setRulesMap(Map<String, List<R>> rulesMap) {
        AssertUtil.notNull(rulesMap, "rulesMap cannot be null");
        this.rulesMap = rulesMap;
    }

    public void addRule(R rule) {
        AssertUtil.notNull(rulesMap, "RuleManager has not been initialized");
        List<R> rules = rulesMap.computeIfAbsent(rule.getResource(), k -> new ArrayList<>());
        ArrayList<R> newRules = new ArrayList<>(rules);
        newRules.add(rule);
        rulesMap.put(rule.getResource(), newRules);
    }

    public void removeRule(R rule) {
        AssertUtil.notNull(rulesMap, "RuleManager has not been initialized");
        List<R> rules = rulesMap.get(rule.getResource());
        if (rules != null) {
            rules.remove(rule);
        }
    }

}

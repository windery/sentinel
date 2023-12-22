package cn.windery.sentinel;


import cn.windery.sentinel.slots.degrade.DegradeRule;
import cn.windery.sentinel.slots.flow.FlowRule;

import java.util.ArrayList;
import java.util.List;

public class ResourceRules {

    private volatile List<DegradeRule> degradeRules = new ArrayList<>();
    private volatile List<FlowRule> flowRules = new ArrayList<>();

    public List<DegradeRule> getDegradeRules() {
        return degradeRules;
    }

    public void setDegradeRules(List<DegradeRule> degradeRules) {
        this.degradeRules = degradeRules;
    }

    public List<FlowRule> getFlowRules() {
        return flowRules;
    }

    public void setFlowRules(List<FlowRule> flowRules) {
        this.flowRules = flowRules;
    }
}

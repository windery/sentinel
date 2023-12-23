package cn.windery.sentinel.sentinel.slots;

import cn.windery.sentinel.sentinel.RuleType;

public interface Rule {

    String getResource();

    RuleType getType();

}

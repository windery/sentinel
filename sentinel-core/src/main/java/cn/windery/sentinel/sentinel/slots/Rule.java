package cn.windery.sentinel.sentinel.slots;

import cn.windery.learning.base.sentinel.RuleType;

public interface Rule {

    String getResource();

    RuleType getType();

}

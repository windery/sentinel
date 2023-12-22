package cn.windery.sentinel.exception;

import cn.windery.sentinel.slots.degrade.DegradeRule;

public class DegradeException extends BlockException {

    private DegradeRule rule;
    private long requests;
    private double rt;

    public DegradeException(String message, DegradeRule rule) {
        super(message);
        this.rule = rule;
    }

    public DegradeRule getRule() {
        return rule;
    }

}

package cn.windery.sentinel.sentinel.slots.degrade;


import cn.windery.sentinel.sentinel.RuleType;
import cn.windery.sentinel.sentinel.slots.AbstractRule;

public class DegradeRule extends AbstractRule {

    protected double threshold;
    protected long retryWaitTime;
    protected long minRequests;
    protected long maxRt;


    public DegradeRule(String resource) {
        super(resource, RuleType.DEGRADE);
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public long getRetryWaitTime() {
        return retryWaitTime;
    }

    public void setRetryWaitTime(long retryWaitTime) {
        this.retryWaitTime = retryWaitTime;
    }

    public long getMinRequests() {
        return minRequests;
    }

    public void setMinRequests(long minRequests) {
        this.minRequests = minRequests;
    }

    public long getMaxRt() {
        return maxRt;
    }

    public void setMaxRt(long maxRt) {
        this.maxRt = maxRt;
    }

}

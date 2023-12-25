package cn.windery.sentinel.slots.degrade;


import cn.windery.sentinel.RuleType;
import cn.windery.sentinel.slots.AbstractRule;

public class DegradeRule extends AbstractRule {

    protected double threshold;
    protected long retryWaitTime;
    protected int minRequests;
    protected int maxRt;
    protected int windowLengthInMs;
    protected int sampleCount;

    public DegradeRule(String resource) {
        super(resource, RuleType.DEGRADE.name().toLowerCase());
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

    public int getMinRequests() {
        return minRequests;
    }

    public void setMinRequests(int minRequests) {
        this.minRequests = minRequests;
    }

    public int getMaxRt() {
        return maxRt;
    }

    public void setMaxRt(int maxRt) {
        this.maxRt = maxRt;
    }

    public int getWindowLengthInMs() {
        return windowLengthInMs;
    }

    public void setWindowLengthInMs(int windowLengthInMs) {
        this.windowLengthInMs = windowLengthInMs;
    }

    public int getSampleCount() {
        return sampleCount;
    }

    public void setSampleCount(int sampleCount) {
        this.sampleCount = sampleCount;
    }

    @Override
    public String toString() {
        return "DegradeRule{" +
                "threshold=" + threshold +
                ", retryWaitTime=" + retryWaitTime +
                ", minRequests=" + minRequests +
                ", maxRt=" + maxRt +
                ", windowLengthInMs=" + windowLengthInMs +
                ", sampleCount=" + sampleCount +
                ", resource='" + resource + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}

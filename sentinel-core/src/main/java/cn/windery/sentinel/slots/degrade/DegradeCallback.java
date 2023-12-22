package cn.windery.sentinel.slots.degrade;

public interface DegradeCallback {

    void fromOpenToHalfOpen(String resource, Object statistic);

    void fromClosedToOpen(String resource, Object statistic);

    void fromHalfOpenToOpen(String resource, Object statistic);

    void fromHalfOpenToClosed(String resource, Object statistic);

}

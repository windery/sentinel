package cn.windery.sentinel.sentinel.slots.statistic;

public interface SecondMetric {

    long pass();

    long block();

    long exception();

    long success();

    long rt();

}

package cn.windery.sentinel.sentinel.adapter;

public interface ResourceAdapter<T> {

    String getResource(T source);

}

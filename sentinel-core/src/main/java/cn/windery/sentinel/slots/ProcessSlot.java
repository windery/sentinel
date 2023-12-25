package cn.windery.sentinel.slots;

import cn.windery.sentinel.Context;

public interface ProcessSlot<T> {

    void entry(Context context, String resource);

    void fireEntry(Context context, String resource);

    void exit(Context context, String resource);

    void fireExit(Context context, String resource);

}

package cn.windery.sentinel.sentinel.slots.statistic;

import cn.windery.sentinel.sentinel.WindowWrap;
import cn.windery.sentinel.sentinel.util.AssertUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.ReentrantLock;

public abstract class LeapArray<T> {

    /**
     * 时间窗口内每个桶的时间长度
     */
    protected int windowLengthInMs;
    /**
     * 时间窗口内的桶的数量
     */
    protected int sampleCount;
    /**
     * 时间窗口的毫秒数
     */
    protected int intervalInMs;

    protected final AtomicReferenceArray<WindowWrap<T>> array;

    protected final ReentrantLock updateLock = new ReentrantLock();


    public LeapArray(int sampleCount, int intervalInMs) {
        AssertUtil.isTrue(sampleCount > 0, "bucket count is invalid");
        AssertUtil.isTrue(intervalInMs > 0, "window interval is invalid");
        AssertUtil.isTrue(intervalInMs % sampleCount == 0, "time span needs to be evenly divided");
        this.sampleCount = sampleCount;
        this.intervalInMs = intervalInMs;
        this.windowLengthInMs = intervalInMs / sampleCount;
        this.array = new AtomicReferenceArray(sampleCount);
    }

    protected abstract void resetWindowTo(WindowWrap<T> windowWrap, long startTime);

    public int calculateTimeIdx(long timeMillis) {
        return (int) ((timeMillis / windowLengthInMs) % sampleCount);
    }

    public long calculateWindowStart(long timeMillis) {
        return timeMillis - timeMillis % windowLengthInMs;
    }

    abstract public WindowWrap<T> newEmptyBucket(long windowStart, int windowLengthInMs);

    public WindowWrap<T> currentWindow(long timeMillis) {
        int windowIndex = calculateTimeIdx(timeMillis);
        long windowStart = calculateWindowStart(timeMillis);
        WindowWrap<T> old = array.get(windowIndex);

        while (true) {
            if (old == null) {
                WindowWrap<T> newWindow = newEmptyBucket(windowStart, windowLengthInMs);
                if (array.compareAndSet(windowIndex, null, newWindow)) {
                    return newWindow;
                } else {
                    Thread.yield();
                }
            } else if (windowStart == old.getWindowStart()) {
                return old;
            } else if (windowStart > old.getWindowStart()) {
                if (updateLock.tryLock()) {
                    try {
                        resetWindowTo(old, windowStart);
                    } finally {
                        updateLock.unlock();
                    }
                } else {
                    Thread.yield();
                }
            } else {
                throw new RuntimeException("windowStart should not be behind existing windowStart");
            }
        }
    }


    public int getWindowLengthInMs() {
        return windowLengthInMs;
    }

    public int getSampleCount() {
        return sampleCount;
    }

    public int getIntervalInMs() {
        return intervalInMs;
    }

    public boolean isWindowDeprecated(WindowWrap<T> wrap, long timeMillis) {
        return timeMillis - wrap.getWindowStart() > intervalInMs;
    }

    public List<T> values(long timeMillis) {
        List<T> list = new ArrayList<T>();
        for (int i = 0; i < array.length(); i++) {
            WindowWrap<T> window = array.get(i);
            if (window == null || isWindowDeprecated(window, timeMillis)) {
                continue;
            }
            list.add(window.getValue());
        }
        return list;
    }

}

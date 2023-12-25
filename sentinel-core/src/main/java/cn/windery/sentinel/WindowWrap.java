package cn.windery.sentinel;

public class WindowWrap<T> {

    private long windowStart;
    private long windowLengthInMs;
    private T value;

    public WindowWrap(long windowStart, long windowLengthInMs, T value) {
        this.windowStart = windowStart;
        this.windowLengthInMs = windowLengthInMs;
        this.value = value;
    }

    public long getWindowStart() {
        return windowStart;
    }

    public long getWindowLengthInMs() {
        return windowLengthInMs;
    }

    public T getValue() {
        return value;
    }

    public boolean isTimeInWindow(long time) {
        return time >= windowStart && time < windowStart + windowLengthInMs;
    }

    public void resetTo(long startTime) {
        this.windowStart = startTime;
    }

}

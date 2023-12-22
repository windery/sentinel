package cn.windery.sentinel.sentinel;

public class Tracer {

    public static void trace(Throwable t) {
        Context context = ContextUtil.getContext();
        if (context != null) {
            context.setError(t);
        }
    }

}

package cn.windery.sentinel;

public class ContextUtil {

    private static final ThreadLocal<Context> CONTEXT_HOLDER = ThreadLocal.withInitial(Context::new);

    public static Context getContext() {
        return CONTEXT_HOLDER.get();
    }

    public static void clearContext() {
        CONTEXT_HOLDER.remove();
    }

}

package cn.windery.sentinel.slots;

import cn.windery.sentinel.Context;

public abstract class AbstractLinkedProcessSlot<T> implements ProcessSlot<T> {

    private AbstractLinkedProcessSlot<?> next = null;

    @Override
    public void fireEntry(Context context, String resource) {

        if (this.next != null) {
            next.entry(context, resource);
        }

    }

    @Override
    public void fireExit(Context context, String resource) {

        if (this.next != null) {
            next.exit(context, resource);
        }

    }

    public AbstractLinkedProcessSlot<?> getNext() {
        return next;
    }

    public void setNext(AbstractLinkedProcessSlot<?> next) {
        this.next = next;
    }

}

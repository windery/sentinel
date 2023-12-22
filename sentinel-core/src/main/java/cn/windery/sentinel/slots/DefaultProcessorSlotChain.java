package cn.windery.sentinel.slots;

import cn.windery.sentinel.Context;

public class DefaultProcessorSlotChain extends ProcessorSlotChain {

    AbstractLinkedProcessSlot<?> first = new AbstractLinkedProcessSlot<Object>() {
        @Override
        public void entry(Context context, String resource) {
            super.fireEntry(context, resource);
        }

        @Override
        public void exit(Context context, String resource) {
            super.fireExit(context, resource);
        }
    };

    AbstractLinkedProcessSlot<?> end = first;

    @Override
    public void entry(Context context, String resource) {
        first.entry(context, resource);
    }

    @Override
    public void exit(Context context, String resource) {
        first.exit(context, resource);
    }

    public AbstractLinkedProcessSlot<?> getFirst() {
        return first;
    }

    @Override
    public void addFirst(AbstractLinkedProcessSlot<?> slot) {
        slot.setNext(first.getNext());
        first.setNext(slot);
        if (end == first) {
            end = slot;
        }
    }

    @Override
    public void addLast(AbstractLinkedProcessSlot<?> slot) {
        end.setNext(slot);
        end = slot;
    }

}

package cn.windery.sentinel.slots;

public abstract class ProcessorSlotChain extends AbstractLinkedProcessSlot<Object> {

    public abstract void addFirst(AbstractLinkedProcessSlot<?> slot);

    public abstract void addLast(AbstractLinkedProcessSlot<?> slot);


}

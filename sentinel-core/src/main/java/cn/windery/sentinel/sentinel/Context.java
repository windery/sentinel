package cn.windery.sentinel.sentinel;

import cn.windery.learning.base.sentinel.exception.BlockException;
import cn.windery.learning.base.sentinel.slots.StatisticNode;

public class Context {

    private StatisticNode node;

    private long entryTime;

    private BlockException blockError;

    private Throwable error;

    public StatisticNode getNode() {
        return node;
    }

    public void setNode(StatisticNode node) {
        this.node = node;
    }

    public BlockException getBlockError() {
        return blockError;
    }

    public void setBlockError(BlockException blockError) {
        this.blockError = blockError;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public long getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(long entryTime) {
        this.entryTime = entryTime;
    }
}

package cn.windery.sentinel;

import cn.windery.sentinel.exception.BlockException;
import cn.windery.sentinel.slots.StatisticNode;

public class Context {

    private StatisticNode node;

    private long entryTime;

    private BlockException blockError;

    private Throwable error;

    private boolean recoverRequest;

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

    public boolean isRecoverRequest() {
        return recoverRequest;
    }

    public void setRecoverRequest(boolean recoverRequest) {
        this.recoverRequest = recoverRequest;
    }
}

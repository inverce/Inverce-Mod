package com.inverce.utils.threads;

public abstract class VARunnable implements Runnable {

    protected long groupId = -1, taskId = -1;

    public VARunnable() { }
    public VARunnable(long groupId) {
        this.groupId = groupId;
    }
    public VARunnable(long groupId, long taskId) {
        this.groupId = groupId;
        this.taskId = taskId;
    }
    public void clear() { }

    @Override
    public String toString() {
        String base = super.toString().replace("com.vendimob.adsdk.", "");
        return "[VA{groupId=" + groupId + ", taskId=" + taskId + "}]" + base;
    }
}

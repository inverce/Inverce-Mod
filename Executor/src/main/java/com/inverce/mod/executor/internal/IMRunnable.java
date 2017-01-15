//package com.inverce.mod.executor.internal;
//
//public abstract class IMRunnable implements Runnable {
//
//    protected long groupId = -1, taskId = -1;
//
//    public IMRunnable() { }
//    public IMRunnable(long groupId) {
//        this.groupId = groupId;
//    }
//    public IMRunnable(long groupId, long taskId) {
//        this.groupId = groupId;
//        this.taskId = taskId;
//    }
//    public void clear() { }
//
//    @Override
//    public String toString() {
//        String base = super.toString().replace("com.vendimob.adsdk.", "");
//        return "[VA{groupId=" + groupId + ", taskId=" + taskId + "}]" + base;
//    }
//}

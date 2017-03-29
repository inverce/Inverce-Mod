package com.inverce.mod.events;

import android.util.SparseArray;

public class ChannelGroup {
    private SparseArray<Channel> channels;

    public ChannelGroup() {
        this.channels = new SparseArray<>();
    }

    public Channel on(int channelId) {
        Channel channel = channels.get(channelId);
        if (channel != null) {
            return channel;
        }
        channels.put(channelId, channel = new Channel());
        return channel;
    }

}

package com.inverce.mod.events;

import android.util.SparseArray;

public class ChannelGroup {
    protected final boolean useWeekEvents;
    protected SparseArray<Channel> channels;

    public ChannelGroup() {
        this(true);
    }

    public ChannelGroup(boolean useWeekEvents) {
        this.useWeekEvents = useWeekEvents;
        this.channels = new SparseArray<>();
    }

    public Channel on(int channelId) {
        Channel channel = channels.get(channelId);
        if (channel != null) {
            return channel;
        }
        channels.put(channelId, channel = new Channel(useWeekEvents));
        return channel;
    }

}

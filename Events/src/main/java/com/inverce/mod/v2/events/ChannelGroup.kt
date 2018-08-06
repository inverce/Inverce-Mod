package com.inverce.mod.v2.events

import android.util.SparseArray

internal interface IChannelGroup {
    fun on(channelId: Int): Channel
}

open class ChannelGroup(protected val useWeekEvents: Boolean = true): IChannelGroup {
    protected var channels = SparseArray<Channel>()

    override fun on(channelId: Int): Channel {
        return channels.get(channelId) ?: Channel(useWeekEvents).apply {
            channels.put(channelId, this)
        }
    }
}
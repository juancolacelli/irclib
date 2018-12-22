package com.colacelli.irclib.connection.listeners

import com.colacelli.irclib.connection.Connection
import com.colacelli.irclib.messages.ChannelNoticeMessage

interface OnChannelNoticeMessageListener : Listener {
    fun onChannelNoticeMessage(connection: Connection, message: ChannelNoticeMessage)

    override val type: Listener.Type
        get() = TYPE

    companion object {
        val TYPE = Listener.Type.CHANNEL_NOTICE
    }
}

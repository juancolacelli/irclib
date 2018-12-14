package com.colacelli.irclib.connection.listeners

import com.colacelli.irclib.connection.Connection
import com.colacelli.irclib.messages.ChannelMessage

interface OnChannelMessageListener : Listener {
    fun onChannelMessage(connection: Connection, message: ChannelMessage)

    override val type: Listener.Type
        get() = TYPE

    companion object {
        val TYPE = Listener.Type.CHANNEL_MESSAGE
    }
}

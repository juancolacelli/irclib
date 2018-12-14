package com.colacelli.irclib.connection.listeners

import com.colacelli.irclib.actors.Channel
import com.colacelli.irclib.connection.Connection
import com.colacelli.irclib.connection.listeners.Listener.Companion.TYPE

interface OnChannelModeListener : Listener {
    fun onChannelMode(connection: Connection, channel: Channel, mode: String)

    override val type: Listener.Type
        get() = TYPE

    companion object {
        val TYPE = Listener.Type.CHANNEL_MODE
    }
}

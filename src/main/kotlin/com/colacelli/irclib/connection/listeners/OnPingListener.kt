package com.colacelli.irclib.connection.listeners

import com.colacelli.irclib.connection.Connection

interface OnPingListener : Listener {
    fun onPing(connection: Connection)

    override val type: Listener.Type
        get() = TYPE

    companion object {
        val TYPE = Listener.Type.PING
    }
}

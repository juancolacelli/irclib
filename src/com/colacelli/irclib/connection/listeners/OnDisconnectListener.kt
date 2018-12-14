package com.colacelli.irclib.connection.listeners

import com.colacelli.irclib.connection.Connection
import com.colacelli.irclib.connection.Server

interface OnDisconnectListener : Listener {
    fun onDisconnect(connection: Connection, server: Server)

    override val type: Listener.Type
        get() = TYPE

    companion object {
        val TYPE = Listener.Type.DISCONNECT
    }
}

package com.colacelli.irclib.connection.listeners

import com.colacelli.irclib.actors.User
import com.colacelli.irclib.connection.Connection
import com.colacelli.irclib.connection.Server

interface OnConnectListener : Listener {
    fun onConnect(connection: Connection, server: Server, user: User)

    override val type: Listener.Type
        get() = TYPE

    companion object {
        val TYPE = Listener.Type.CONNECT
    }
}

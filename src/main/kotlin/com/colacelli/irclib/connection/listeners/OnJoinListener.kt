package com.colacelli.irclib.connection.listeners

import com.colacelli.irclib.actors.Channel
import com.colacelli.irclib.actors.User
import com.colacelli.irclib.connection.Connection

interface OnJoinListener : Listener {
    fun onJoin(connection: Connection, user: User, channel: Channel)

    override val type: Listener.Type
        get() = TYPE

    companion object {
        val TYPE = Listener.Type.JOIN
    }
}

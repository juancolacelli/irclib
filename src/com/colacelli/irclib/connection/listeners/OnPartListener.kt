package com.colacelli.irclib.connection.listeners

import com.colacelli.irclib.actors.Channel
import com.colacelli.irclib.actors.User
import com.colacelli.irclib.connection.Connection

interface OnPartListener : Listener {
    fun onPart(connection: Connection, user: User, channel: Channel)

    override val type: Listener.Type
        get() = TYPE

    companion object {
        val TYPE = Listener.Type.PART
    }
}

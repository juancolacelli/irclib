package com.colacelli.irclib.connection.listeners

import com.colacelli.irclib.actors.User
import com.colacelli.irclib.connection.Connection

interface OnNickChangeListener : Listener {
    fun onNickChange(connection: Connection, user: User)

    override val type: Listener.Type
        get() = TYPE

    companion object {
        val TYPE = Listener.Type.NICK
    }
}

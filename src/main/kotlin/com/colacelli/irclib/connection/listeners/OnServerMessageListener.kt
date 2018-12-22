package com.colacelli.irclib.connection.listeners

import com.colacelli.irclib.connection.Connection

interface OnServerMessageListener : Listener {
    fun serverMessage(): String
    fun onServerMessage(connection: Connection, message: String, command: String, args: List<String>)

    override val type: Listener.Type
        get() = TYPE

    companion object {
        val TYPE = Listener.Type.SERVER_MESSAGE
    }
}

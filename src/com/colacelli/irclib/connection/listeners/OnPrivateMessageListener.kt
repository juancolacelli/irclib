package com.colacelli.irclib.connection.listeners

import com.colacelli.irclib.connection.Connection
import com.colacelli.irclib.messages.PrivateMessage

interface OnPrivateMessageListener : Listener {
    fun onPrivateMessage(connection: Connection, message: PrivateMessage)

    override val type: Listener.Type
        get() = TYPE

    companion object {
        val TYPE = Listener.Type.PRIVATE_MESSAGE
    }
}

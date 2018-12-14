package com.colacelli.irclib.connection.listeners

import com.colacelli.irclib.connection.Connection
import com.colacelli.irclib.messages.CTCPMessage

interface OnCTCPListener : Listener {
    fun onCTCP(connection: Connection, message: CTCPMessage, vararg args: List<String>)

    override val type: Listener.Type
        get() = TYPE

    companion object {
        val TYPE = Listener.Type.CONNECT
        const val CTCP_CHARACTER = "\u0001"
    }
}

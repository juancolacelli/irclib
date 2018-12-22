package com.colacelli.irclib.connection.listeners

import com.colacelli.irclib.connection.Connection
import com.colacelli.irclib.messages.PrivateNoticeMessage

interface OnPrivateNoticeMessageListener : Listener {
    fun onNoticeMessage(connection: Connection, message: PrivateNoticeMessage)

    override val type: Listener.Type
        get() = TYPE

    companion object {
        val TYPE = Listener.Type.PRIVATE_NOTICE
    }
}

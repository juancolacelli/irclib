package com.colacelli.irclib.messages

import com.colacelli.irclib.actors.User
import com.colacelli.irclib.connection.listeners.OnCTCPListener

class CTCPMessage(text: String, val command: String, sender: User?, receiver: User?) : PrivateMessage(text, sender, receiver) {
    override fun toString(): String {
        return "${OnCTCPListener.CTCP_CHARACTER} $command $text${OnCTCPListener.CTCP_CHARACTER}"
    }
}
package com.colacelli.irclib.connection.listeners

interface Listener {
    enum class Type {
        LISTENER,
        CHANNEL_MESSAGE,
        CHANNEL_MODE,
        CHANNEL_NOTICE,
        CONNECT,
        CTCP,
        DISCONNECT,
        JOIN,
        KICK,
        NICK,
        PART,
        PING,
        PRIVATE_MESSAGE,
        PRIVATE_NOTICE,
        RAW_CODE,
        SERVER_MESSAGE
    }

    val type: Type
        get() = TYPE

    companion object {
        val TYPE = Type.LISTENER
    }
}

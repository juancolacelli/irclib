package com.colacelli.irclib.connection.listeners;

public interface Listener {
    enum Type {
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

    Type TYPE = Type.LISTENER;

    Type getType();
}

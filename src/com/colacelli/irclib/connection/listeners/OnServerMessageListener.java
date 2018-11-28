package com.colacelli.irclib.connection.listeners;

import com.colacelli.irclib.connection.Connection;

public interface OnServerMessageListener extends Listener {
    Type TYPE = Type.SERVER_MESSAGE;

    String serverMessage();

    void onServerMessage(Connection connection, String message, String command, String... args);

    @Override
    default Type getType() {
        return TYPE;
    }
}

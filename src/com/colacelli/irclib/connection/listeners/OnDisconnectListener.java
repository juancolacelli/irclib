package com.colacelli.irclib.connection.listeners;

import com.colacelli.irclib.connection.Connection;
import com.colacelli.irclib.connection.Server;

public interface OnDisconnectListener extends Listener {
    Type TYPE = Type.DISCONNECT;

    void onDisconnect(Connection connection, Server server);

    @Override
    default Type getType() {
        return TYPE;
    }
}

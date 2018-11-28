package com.colacelli.irclib.connection.listeners;

import com.colacelli.irclib.actors.User;
import com.colacelli.irclib.connection.Connection;
import com.colacelli.irclib.connection.Server;

public interface OnConnectListener extends Listener {
    Type TYPE = Type.CONNECT;

    void onConnect(Connection connection, Server server, User user);

    @Override
    default Type getType() {
        return TYPE;
    }
}

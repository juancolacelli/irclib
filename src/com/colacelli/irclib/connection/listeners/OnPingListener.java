package com.colacelli.irclib.connection.listeners;

import com.colacelli.irclib.connection.Connection;

public interface OnPingListener extends Listener {
    Type TYPE = Type.PING;

    void onPing(Connection connection);

    @Override
    default Type getType() {
        return TYPE;
    }
}

package com.colacelli.irclib.connection.listeners;

import com.colacelli.irclib.actors.Channel;
import com.colacelli.irclib.actors.User;
import com.colacelli.irclib.connection.Connection;

public interface OnJoinListener extends Listener {
    Type TYPE = Type.JOIN;

    void onJoin(Connection connection, User user, Channel channel);

    @Override
    default Type getType() {
        return TYPE;
    }
}

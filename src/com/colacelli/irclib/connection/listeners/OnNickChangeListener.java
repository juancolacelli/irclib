package com.colacelli.irclib.connection.listeners;

import com.colacelli.irclib.actors.User;
import com.colacelli.irclib.connection.Connection;

public interface OnNickChangeListener extends Listener {
    Type TYPE = Type.NICK;

    void onNickChange(Connection connection, User user);

    @Override
    default Type getType() {
        return TYPE;
    }
}

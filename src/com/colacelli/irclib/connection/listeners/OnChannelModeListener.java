package com.colacelli.irclib.connection.listeners;

import com.colacelli.irclib.actors.Channel;
import com.colacelli.irclib.connection.Connection;

public interface OnChannelModeListener extends Listener {
    Type TYPE = Type.CHANNEL_MODE;

    void onChannelMode(Connection connection, Channel channel, String mode);

    @Override
    default Type getType() {
        return TYPE;
    }
}

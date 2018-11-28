package com.colacelli.irclib.connection.listeners;

import com.colacelli.irclib.connection.Connection;
import com.colacelli.irclib.messages.ChannelMessage;

public interface OnChannelMessageListener extends Listener {
    Type TYPE = Type.CHANNEL_MESSAGE;

    @Override
    default Type getType() {
        return TYPE;
    }

    void onChannelMessage(Connection connection, ChannelMessage message);
}

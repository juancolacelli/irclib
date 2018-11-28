package com.colacelli.irclib.connection.listeners;

import com.colacelli.irclib.connection.Connection;
import com.colacelli.irclib.messages.PrivateMessage;

public interface OnPrivateMessageListener extends Listener {
    Type TYPE = Type.PRIVATE_MESSAGE;

    void onPrivateMessage(Connection connection, PrivateMessage message);

    @Override
    default Type getType() {
        return TYPE;
    }
}

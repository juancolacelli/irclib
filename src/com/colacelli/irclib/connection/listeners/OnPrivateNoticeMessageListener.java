package com.colacelli.irclib.connection.listeners;

import com.colacelli.irclib.connection.Connection;
import com.colacelli.irclib.messages.PrivateNoticeMessage;

public interface OnPrivateNoticeMessageListener extends Listener {
    Type TYPE = Type.PRIVATE_NOTICE;

    void onNoticeMessage(Connection connection, PrivateNoticeMessage message);

    @Override
    default Type getType() {
        return TYPE;
    }
}

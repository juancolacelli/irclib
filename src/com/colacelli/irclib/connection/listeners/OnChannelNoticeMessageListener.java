package com.colacelli.irclib.connection.listeners;

import com.colacelli.irclib.connection.Connection;
import com.colacelli.irclib.messages.ChannelNoticeMessage;

public interface OnChannelNoticeMessageListener extends Listener {
    Type TYPE = Type.CHANNEL_NOTICE;

    void onChannelNoticeMessage(Connection connection, ChannelNoticeMessage message);

    @Override
    default Type getType() {
        return TYPE;
    }
}

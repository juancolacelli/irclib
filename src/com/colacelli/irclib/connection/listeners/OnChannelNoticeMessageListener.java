package com.colacelli.irclib.connection.listeners;

import com.colacelli.irclib.connection.Connection;
import com.colacelli.irclib.messages.ChannelNoticeMessage;

public interface OnChannelNoticeMessageListener {
    void onChannelNoticeMessage(Connection connection, ChannelNoticeMessage message);
}

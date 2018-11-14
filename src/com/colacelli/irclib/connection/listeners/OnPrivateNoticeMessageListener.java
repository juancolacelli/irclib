package com.colacelli.irclib.connection.listeners;

import com.colacelli.irclib.connection.Connection;
import com.colacelli.irclib.messages.PrivateNoticeMessage;

public interface OnPrivateNoticeMessageListener {
    void onNoticeMessage(Connection connection, PrivateNoticeMessage message);
}

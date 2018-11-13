package com.colacelli.irclib.connection.listeners;

import com.colacelli.irclib.connection.Connection;
import com.colacelli.irclib.messages.CTCPMessage;

public interface OnCtcpListener {
    String CTCP_CHARACTER = "\u0001";

    void onCtcp(Connection connection, CTCPMessage message, String... args);
}

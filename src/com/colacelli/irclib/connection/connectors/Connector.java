package com.colacelli.irclib.connection.connectors;

import com.colacelli.irclib.actors.User;
import com.colacelli.irclib.connection.Server;

import java.io.IOException;

public interface Connector {
    String ENTER = "\r\n";

    void connect(Server newServer, User newUser) throws IOException;

    void disconnect() throws IOException;

    String listen() throws IOException;

    void send(String text) throws IOException;
}

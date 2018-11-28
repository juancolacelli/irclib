package com.colacelli.irclib.connection.listeners;

public interface Listenable {
    void addListener(Listener listener);
    void removeListener(Listener listener);
}

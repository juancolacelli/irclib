package com.colacelli.irclib.connection;

import com.colacelli.irclib.connection.listeners.Listenable;
import com.colacelli.irclib.connection.listeners.Listener;

import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionListener implements Listenable {
    private HashMap<Listener.Type, ArrayList<Listener>> listeners;

    public ConnectionListener() {
        listeners = new HashMap<>();
    }

    @Override
    public void addListener(Listener listener) {
        ArrayList<Listener> currentListeners = listeners.getOrDefault(listener.getType(), new ArrayList<>());
        currentListeners.add(listener);
        listeners.put(listener.getType(), currentListeners);
    }

    @Override
    public void removeListener(Listener listener) {
        getListeners(listener.getType()).remove(listener);
    }

    protected ArrayList<? extends Listener> getListeners(Listener.Type type) {
        return listeners.getOrDefault(type, new ArrayList<>());
    }
}

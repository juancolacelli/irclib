package com.colacelli.irclib.connection;

import com.colacelli.irclib.connection.listeners.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionListener implements Listenable {
    protected ArrayList<OnChannelMessageListener> onChannelMessageListeners;
    protected ArrayList<OnChannelModeListener> onChannelModeListeners;
    protected ArrayList<OnChannelNoticeMessageListener> onChannelNoticeMessageListeners;
    protected ArrayList<OnConnectListener> onConnectListeners;
    protected ArrayList<OnCtcpListener> onCtcpListeners;
    protected ArrayList<OnDisconnectListener> onDisconnectListeners;
    protected ArrayList<OnJoinListener> onJoinListeners;
    protected ArrayList<OnKickListener> onKickListeners;
    protected ArrayList<OnNickChangeListener> onNickChangeListeners;
    protected ArrayList<OnPartListener> onPartListeners;
    protected ArrayList<OnPingListener> onPingListeners;
    protected ArrayList<OnPrivateMessageListener> onPrivateMessageListeners;
    protected ArrayList<OnPrivateNoticeMessageListener> onPrivateNoticeMessageListeners;
    protected HashMap<Integer, ArrayList<OnRawCodeListener>> onRawCodeListeners;
    protected HashMap<String, ArrayList<OnServerMessageListener>> onServerMessageListeners;

    public ConnectionListener() {
        onChannelMessageListeners = new ArrayList<>();
        onChannelModeListeners = new ArrayList<>();
        onChannelNoticeMessageListeners = new ArrayList<>();
        onConnectListeners = new ArrayList<>();
        onCtcpListeners = new ArrayList<>();
        onDisconnectListeners = new ArrayList<>();
        onJoinListeners = new ArrayList<>();
        onKickListeners = new ArrayList<>();
        onNickChangeListeners = new ArrayList<>();
        onPartListeners = new ArrayList<>();
        onPingListeners = new ArrayList<>();
        onPrivateMessageListeners = new ArrayList<>();
        onPrivateNoticeMessageListeners = new ArrayList<>();
        onRawCodeListeners = new HashMap<>();
        onServerMessageListeners = new HashMap<>();
    }

    @Override
    public void addListener(OnConnectListener listener) {
        onConnectListeners.add(listener);
    }

    @Override
    public void addListener(OnDisconnectListener listener) {
        onDisconnectListeners.add(listener);
    }

    @Override
    public void addListener(OnPingListener listener) {
        onPingListeners.add(listener);
    }

    @Override
    public void addListener(OnJoinListener listener) {
        onJoinListeners.add(listener);
    }

    @Override
    public void addListener(OnPartListener listener) {
        onPartListeners.add(listener);
    }

    @Override
    public void addListener(OnKickListener listener) {
        onKickListeners.add(listener);
    }

    @Override
    public void addListener(OnChannelModeListener listener) {
        onChannelModeListeners.add(listener);
    }

    @Override
    public void addListener(OnChannelMessageListener listener) {
        onChannelMessageListeners.add(listener);
    }

    @Override
    public void addListener(OnPrivateMessageListener listener) {
        onPrivateMessageListeners.add(listener);
    }

    @Override
    public void addListener(OnChannelNoticeMessageListener listener) {
        onChannelNoticeMessageListeners.add(listener);
    }

    @Override
    public void addListener(OnPrivateNoticeMessageListener listener) {
        onPrivateNoticeMessageListeners.add(listener);
    }

    @Override
    public void addListener(OnNickChangeListener listener) {
        onNickChangeListeners.add(listener);
    }

    @Override
    public void addListener(OnCtcpListener listener) {
        onCtcpListeners.add(listener);
    }

    @Override
    public void addListener(int rawCode, OnRawCodeListener listener) {
        ArrayList<OnRawCodeListener> currentListeners = onRawCodeListeners.getOrDefault(rawCode, new ArrayList<>());
        currentListeners.add(listener);

        onRawCodeListeners.put(rawCode, currentListeners);
    }

    @Override
    public void removeListener(OnConnectListener listener) {
        onConnectListeners.remove(listener);
    }

    @Override
    public void removeListener(OnDisconnectListener listener) {
        onDisconnectListeners.remove(listener);
    }

    @Override
    public void removeListener(OnPingListener listener) {
        onPingListeners.remove(listener);
    }

    @Override
    public void removeListener(OnJoinListener listener) {
        onJoinListeners.remove(listener);
    }

    @Override
    public void removeListener(OnPartListener listener) {
        onPartListeners.remove(listener);
    }

    @Override
    public void removeListener(OnKickListener listener) {
        onKickListeners.remove(listener);
    }

    @Override
    public void removeListener(OnChannelModeListener listener) {
        onChannelModeListeners.remove(listener);
    }

    @Override
    public void removeListener(OnChannelMessageListener listener) {
        onChannelMessageListeners.remove(listener);
    }

    @Override
    public void removeListener(OnPrivateMessageListener listener) {
        onPrivateMessageListeners.remove(listener);
    }

    @Override
    public void removeListener(OnChannelNoticeMessageListener listener) {
        onChannelNoticeMessageListeners.remove(listener);
    }

    @Override
    public void removeListener(OnPrivateNoticeMessageListener listener) {
        onPrivateNoticeMessageListeners.remove(listener);
    }

    @Override
    public void removeListener(OnNickChangeListener listener) {
        onNickChangeListeners.remove(listener);
    }

    @Override
    public void removeListener(OnCtcpListener listener) {
        onCtcpListeners.remove(listener);
    }

    @Override
    public void removeListener(int rawCode, OnRawCodeListener listener) {
        onRawCodeListeners.get(rawCode).remove(listener);
    }
}

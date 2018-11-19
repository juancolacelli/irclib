package com.colacelli.irclib.connection.listeners;

public interface Listenable {
    void addListener(OnConnectListener listener);

    void addListener(OnDisconnectListener listener);

    void addListener(OnPingListener listener);

    void addListener(OnJoinListener listener);

    void addListener(OnPartListener listener);

    void addListener(OnKickListener listener);

    void addListener(OnChannelModeListener listener);

    void addListener(OnChannelMessageListener listener);

    void addListener(OnPrivateMessageListener listener);

    void addListener(OnPrivateNoticeMessageListener listener);

    void addListener(OnNickChangeListener listener);

    void addListener(OnCtcpListener listener);

    void addListener(int rawCode, OnRawCodeListener listener);

    void removeListener(OnConnectListener listener);

    void removeListener(OnDisconnectListener listener);

    void removeListener(OnPingListener listener);

    void removeListener(OnJoinListener listener);

    void removeListener(OnPartListener listener);

    void removeListener(OnKickListener listener);

    void removeListener(OnChannelModeListener listener);

    void removeListener(OnChannelMessageListener listener);

    void removeListener(OnPrivateMessageListener listener);

    void removeListener(OnPrivateNoticeMessageListener listener);

    void removeListener(OnNickChangeListener listener);

    void removeListener(OnCtcpListener listener);

    void removeListener(int rawCode, OnRawCodeListener listener);
}

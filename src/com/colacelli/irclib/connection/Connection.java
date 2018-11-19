package com.colacelli.irclib.connection;

import com.colacelli.irclib.actors.Channel;
import com.colacelli.irclib.actors.User;
import com.colacelli.irclib.connection.Rawable.RawCode;
import com.colacelli.irclib.connection.connectors.Connector;
import com.colacelli.irclib.connection.connectors.SecureConnector;
import com.colacelli.irclib.connection.connectors.UnsecureConnector;
import com.colacelli.irclib.connection.listeners.OnRawCodeListener;
import com.colacelli.irclib.connection.listeners.OnServerMessageListener;
import com.colacelli.irclib.messages.CTCPMessage;
import com.colacelli.irclib.messages.ChannelMessage;
import com.colacelli.irclib.messages.PrivateMessage;
import com.colacelli.irclib.messages.PrivateNoticeMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static com.colacelli.irclib.connection.listeners.OnCtcpListener.CTCP_CHARACTER;

public final class Connection extends ConnectionListener {
    private Server server;

    private User user;
    private HashMap<String, Channel> channels = new HashMap<>();

    private Connector connector;

    public Connection() {
        super();

        addListener(RawCode.LOGGED_IN.getCode(), (connection, message, rawCode, args) -> {
            onConnectListeners.forEach((listener) -> listener.onConnect(this, server, user));
        });

        addListener(RawCode.NICKNAME_IN_USE.getCode(), (connection, message, rawCode, args) -> {
            nick(user.getNick() + (new Random()).nextInt(9));
        });

        addListener("ping", (connection, message, command, args) -> {
            send("PONG " + message.substring(5));

            onPingListeners.forEach((listener) -> listener.onPing(this));
        });

        addListener("privmsg", (connection, message, command, args) -> {
            int nickIndex = message.indexOf("!");
            int messageIndex = message.indexOf(":", 1);

            if (nickIndex != -1 && messageIndex != -1) {
                String nick = message.substring(1, nickIndex);
                String login = message.substring(nickIndex + 1, message.indexOf("@"));
                String text = message.substring(messageIndex + 1);

                User.Builder userBuilder = new User.Builder();
                userBuilder
                        .setNick(nick)
                        .setLogin(login);

                Channel channel = channels.get(args[2]);
                if (channel != null) {
                    ChannelMessage.Builder channelMessageBuilder = new ChannelMessage.Builder();
                    channelMessageBuilder
                            .setSender(userBuilder.build())
                            .setChannel(channel)
                            .setText(text);

                    onChannelMessageListeners.forEach((listener) -> listener.onChannelMessage(this, channelMessageBuilder.build()));
                } else {
                    if (text.startsWith(CTCP_CHARACTER) && text.endsWith(CTCP_CHARACTER)) {
                        String ctcp = text.substring(1, text.length() - 1);

                        CTCPMessage.Builder ctcpMessageBuilder = new CTCPMessage.Builder();
                        ctcpMessageBuilder
                                .setSender(userBuilder.build())
                                .setReceiver(user);

                        ctcpMessageBuilder.setCommand(ctcp);

                        onCtcpListeners.forEach(onCtcpListener -> {
                            onCtcpListener.onCtcp(this, ctcpMessageBuilder.build(), args);
                        });
                    } else {
                        PrivateMessage.Builder privateMessageBuilder = new PrivateMessage.Builder();
                        privateMessageBuilder
                                .setSender(userBuilder.build())
                                .setReceiver(user)
                                .setText(text);

                        onPrivateMessageListeners.forEach((listener) -> listener.onPrivateMessage(this, privateMessageBuilder.build()));
                    }
                }
            }
        });

        addListener("join", (connection, message, command, args) -> {
            Channel channel = channels.get(args[2].substring(1));

            if (channel != null) {
                User user = new User(message.substring(1, message.indexOf("!")));
                onJoinListeners.forEach((listener) -> listener.onJoin(this, user, channel));
            }
        });

        addListener("kick", (connection, message, command, args) -> {
            Channel channel = channels.get(args[2]);

            if (channel != null) {
                User user = new User(args[3]);
                onKickListeners.forEach((listener) -> listener.onKick(this, user, channel));
            }
        });

        addListener("mode", (connection, message, command, args) -> {
            Channel channel = channels.get(args[2]);

            if (channel != null) {
                onChannelModeListeners.forEach((listener) -> listener.onChannelMode(this, channel, args[3]));
            }
        });

        addListener("nick", (connection, message, command, args) -> {
            String oldNick = message.substring(1, message.indexOf("!"));

            User.Builder userBuilder = new User.Builder();
            userBuilder.setNick(oldNick);

            User nickUser = userBuilder.build();
            nickUser.setNick(args[2].substring(1));

            onNickChangeListeners.forEach((listener) -> listener.onNickChange(this, nickUser));
        });

        addListener("part", (connection, message, command, args) -> {
            Channel channel = channels.get(args[2]);

            if (channel != null) {
                User user = new User(message.substring(1, message.indexOf("!")));
                onPartListeners.forEach((listener) -> listener.onPart(this, user, channel));
            }
        });
    }

    public void connect(Server newServer, User newUser) {
        try {
            user = newUser;
            server = newServer;

            if (server.isSecure()) {
                connector = new SecureConnector();
            } else {
                connector = new UnsecureConnector();
            }

            connector.connect(server, user);

            if (!server.getPassword().equals("")) {
                send("PASS " + server.getPassword());
            }

            login(user);
        } catch (Exception e) {
            e.printStackTrace();
            reconnect();
        }
    }

    public void disconnect() {
        onDisconnectListeners.forEach((listener) -> listener.onDisconnect(this, server));
    }

    public void join(Channel channel) {
        send("JOIN " + channel.getName());

        channels.putIfAbsent(channel.getName(), channel);
    }

    private void listen() throws IOException {
        // Keep reading lines from the server.
        String line;

        while ((line = connector.listen()) != null) {
            System.out.println(line);

            String[] splittedLine = line.split(" ");

            try {
                // Raw code
                int rawCode = Integer.parseInt(splittedLine[1]);

                ArrayList<OnRawCodeListener> rawCodeListeners = onRawCodeListeners.get(rawCode);
                if (rawCodeListeners != null) {
                    for (int i = 0; i < rawCodeListeners.size(); i++) {
                        OnRawCodeListener rawCodeListener = rawCodeListeners.get(i);
                        rawCodeListener.onRawCode(this, line, rawCode, splittedLine);
                    }
                }

            } catch (NumberFormatException e) {
                // Not a Raw code
                String command = splittedLine[1].toUpperCase();

                // Try with first string (e.g, for PING)
                if (!onServerMessageListeners.containsKey(command)) {
                    command = splittedLine[0].toUpperCase();
                }

                ArrayList<OnServerMessageListener> serverMessageListeners = onServerMessageListeners.get(command);

                if (serverMessageListeners != null)
                    for (OnServerMessageListener onServerMessageListener : serverMessageListeners) {
                        onServerMessageListener.onServerMessage(this, line, command, splittedLine);
                    }
            }
        }

        disconnect();
    }

    private void login(User user) throws IOException {
        nick(user.getNick());

        send("USER " + user.getLogin() + " 8 * : " + user.getRealName());

        listen();
    }

    public void send(String message) {
        System.out.println(message);

        try {
            connector.send(message);
        } catch (IOException e) {
            e.printStackTrace();
            reconnect();
        }
    }

    public void send(ChannelMessage channelMessage) {
        send("PRIVMSG " + channelMessage.getChannel().getName() + " :" + channelMessage.getText());

        channelMessage.setSender(user);
    }

    public void send(PrivateMessage privateMessage) {
        send("PRIVMSG " + privateMessage.getReceiver().getNick() + " :" + privateMessage.getText());

        privateMessage.setSender(user);
    }

    public void send(PrivateNoticeMessage privateNoticeMessage) {
        send("NOTICE " + privateNoticeMessage.getReceiver().getNick() + " :" + privateNoticeMessage.getText());

        privateNoticeMessage.setSender(user);
    }

    public void send(CTCPMessage ctcpMessage) {
        send("NOTICE " + ctcpMessage.getReceiver().getNick() + " :" + ctcpMessage.getCTCPText());

        ctcpMessage.setSender(user);
    }

    public void mode(String mode) {
        send("MODE " + mode);
    }

    public void mode(Channel channel, String mode) {
        send("MODE " + channel.getName() + " " + mode);
    }

    public void whois(User user) {
        send("WHOIS " + user.getNick());
    }

    public void topic(Channel channel, String topic) {
        send("TOPIC " + channel.getName() + " " + topic);
    }

    public void kick(Channel channel, User user, String reason) {
        send("KICK " + channel.getName() + " " + user.getNick() + " " + reason);
    }

    public void nick(String nick) {
        user.setNick(nick);

        send("NICK " + nick);
    }

    public void part(Channel channel) {
        send("PART " + channel.getName());

        if (channels.get(channel.getName()) != null)
            channels.remove(channel.getName());
    }

    private void reconnect() {
        try {
            connector.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            connector.connect(server, user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User getUser() {
        return user;
    }

    public ArrayList<Channel> getChannels() {
        ArrayList<Channel> channels = new ArrayList<>();

        this.channels.forEach((name, channel) -> channels.add(channel));

        return channels;
    }

    private void addListener(String command, OnServerMessageListener listener) {
        command = command.toUpperCase();

        ArrayList<OnServerMessageListener> currentListeners = onServerMessageListeners.get(command);

        if (currentListeners == null) {
            currentListeners = new ArrayList<>();
        }

        currentListeners.add(listener);

        onServerMessageListeners.put(command, currentListeners);
    }
}

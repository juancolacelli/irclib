package com.colacelli.irclib.connection;

import com.colacelli.irclib.actors.Channel;
import com.colacelli.irclib.actors.User;
import com.colacelli.irclib.connection.Rawable.RawCode;
import com.colacelli.irclib.connection.connectors.Connector;
import com.colacelli.irclib.connection.connectors.SecureConnector;
import com.colacelli.irclib.connection.connectors.UnsecureConnector;
import com.colacelli.irclib.connection.listeners.*;
import com.colacelli.irclib.messages.*;

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

        addListener(new OnRawCodeListener() {
            @Override
            public int rawCode() {
                return RawCode.LOGGED_IN.getCode();
            }

            @Override
            public void onRawCode(Connection connection, String message, int rawCode, String... args) {
                getListeners(OnConnectListener.TYPE).forEach(
                        (listener -> ((OnConnectListener) listener).onConnect(connection, server, user)));
            }
        });

        addListener(new OnRawCodeListener() {
            @Override
            public int rawCode() {
                return RawCode.NICKNAME_IN_USE.getCode();
            }

            @Override
            public void onRawCode(Connection connection, String message, int rawCode, String... args) {
                nick(user.getNick() + (new Random()).nextInt(9));
            }
        });

        addListener(new OnServerMessageListener() {
                @Override
                public String serverMessage() {
                    return "PING";
                }

                @Override
                public void onServerMessage(Connection connection, String message, String command, String... args) {
                    send("PONG " + message.substring(5));
                    getListeners(OnPingListener.TYPE).forEach(
                            (listener -> ((OnPingListener) listener).onPing(connection)));
                }
            }
        );

        addListener(new OnServerMessageListener() {
            @Override
            public String serverMessage() {
                return "PRIVMSG";
            }

            @Override
            public void onServerMessage(Connection connection, String message, String command, String... args) {
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

                        getListeners(OnChannelMessageListener.TYPE).forEach(
                                (listener -> ((OnChannelMessageListener) listener).onChannelMessage(connection, channelMessageBuilder.build())));
                    } else {
                        if (text.startsWith(CTCP_CHARACTER) && text.endsWith(CTCP_CHARACTER)) {
                            String ctcp = text.substring(1, text.length() - 1);

                            CTCPMessage.Builder ctcpMessageBuilder = new CTCPMessage.Builder();
                            ctcpMessageBuilder
                                    .setSender(userBuilder.build())
                                    .setReceiver(user);

                            ctcpMessageBuilder.setCommand(ctcp);

                            getListeners(OnCtcpListener.TYPE).forEach(
                                    (listener -> ((OnCtcpListener) listener).onCtcp(connection, ctcpMessageBuilder.build(), args)));
                        } else {
                            PrivateMessage.Builder privateMessageBuilder = new PrivateMessage.Builder();
                            privateMessageBuilder
                                    .setSender(userBuilder.build())
                                    .setReceiver(user)
                                    .setText(text);

                            getListeners(OnPrivateMessageListener.TYPE).forEach(
                                    (listener -> ((OnPrivateMessageListener) listener).onPrivateMessage(connection, privateMessageBuilder.build())));
                        }
                    }
                }
            }
        });

        addListener(new OnServerMessageListener() {
            @Override
            public String serverMessage() {
                return "JOIN";
            }

            @Override
            public void onServerMessage(Connection connection, String message, String command, String... args) {
                Channel channel = channels.get(args[2].substring(1));

                if (channel != null) {
                    User user = new User(message.substring(1, message.indexOf("!")));
                    getListeners(OnJoinListener.TYPE).forEach(
                            (listener -> ((OnJoinListener) listener).onJoin(connection, user, channel)));
                }
            }
        });

        addListener(new OnServerMessageListener() {
            @Override
            public String serverMessage() {
                return "KICK";
            }

            @Override
            public void onServerMessage(Connection connection, String message, String command, String... args) {
                Channel channel = channels.get(args[2]);

                if (channel != null) {
                    User user = new User(args[3]);
                    getListeners(OnKickListener.TYPE).forEach(
                            (listener -> ((OnKickListener) listener).onKick(connection, user, channel)));
                }
            }
        });

        addListener(new OnServerMessageListener() {
            @Override
            public String serverMessage() {
                return "MODE";
            }

            @Override
            public void onServerMessage(Connection connection, String message, String command, String... args) {
                Channel channel = channels.get(args[2]);

                if (channel != null) {
                    getListeners(OnChannelModeListener.TYPE).forEach(
                            (listener -> ((OnChannelModeListener) listener).onChannelMode(connection, channel, args[3])));
                }
            }
        });

        addListener(new OnServerMessageListener() {
            @Override
            public String serverMessage() {
                return "NICK";
            }

            @Override
            public void onServerMessage(Connection connection, String message, String command, String... args) {
                String oldNick = message.substring(1, message.indexOf("!"));

                User.Builder userBuilder = new User.Builder();
                userBuilder.setNick(oldNick);

                User nickUser = userBuilder.build();
                nickUser.setNick(args[2].substring(1));

                getListeners(OnNickChangeListener.TYPE).forEach(
                        (listener -> ((OnNickChangeListener) listener).onNickChange(connection, nickUser)));
            }
        });

        addListener(new OnServerMessageListener() {
            @Override
            public String serverMessage() {
                return "PART";
            }

            @Override
            public void onServerMessage(Connection connection, String message, String command, String... args) {
                Channel channel = channels.get(args[2]);

                if (channel != null) {
                    User user = new User(message.substring(1, message.indexOf("!")));

                    getListeners(OnPartListener.TYPE).forEach(
                            (listener -> ((OnPartListener) listener).onPart(connection, user,channel)));
                }
            }
        });
    }

    public void connect(Server server, User user) {
        try {
            this.user = user;
            this.server = server;

            if (this.server.isSecure()) {
                connector = new SecureConnector();
            } else {
                connector = new UnsecureConnector();
            }

            connector.connect(this.server, this.user);

            if (!this.server.getPassword().equals("")) {
                send("PASS " + this.server.getPassword());
            }

            login(this.user);
        } catch (Exception e) {
            e.printStackTrace();
            reconnect();
        }
    }

    public void disconnect() {
        getListeners(OnDisconnectListener.TYPE).forEach(
                (listener -> ((OnDisconnectListener) listener).onDisconnect(this, server)));
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

                ArrayList<? extends Listener> rawCodeListeners = getListeners(OnRawCodeListener.TYPE);
                // Must use for instead of foreach to avoid ConcurrentModificationException
                for(int i = 0; i < rawCodeListeners.size(); i++) {
                    OnRawCodeListener rawCodeListener = (OnRawCodeListener) rawCodeListeners.get(i);
                    if (rawCodeListener.rawCode() == rawCode) rawCodeListener.onRawCode(this, line, rawCode, splittedLine);
                }
            } catch (NumberFormatException e) {
                for(Listener listener : getListeners(OnServerMessageListener.TYPE)) {
                    OnServerMessageListener serverMessageListener = (OnServerMessageListener) listener;

                    // Commands usually are in position 0, but PING is on position 1
                    for (int i = 0; i <= 1; i++) {
                        String command = splittedLine[i].toUpperCase();
                        if (command.equals(serverMessageListener.serverMessage())) serverMessageListener.onServerMessage(this, line, command, splittedLine);
                    }
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

    public void send(ChannelMessage message) {
        send("PRIVMSG " + message.getChannel().getName() + " :" + message.getText());

        message.setSender(user);
    }

    public void send(PrivateMessage message) {
        send("PRIVMSG " + message.getReceiver().getNick() + " :" + message.getText());

        message.setSender(user);
    }

    public void send(ChannelNoticeMessage message) {
        send("NOTICE " + message.getChannel().getName() + " :" + message.getText());

        message.setSender(user);
    }

    public void send(PrivateNoticeMessage message) {
        send("NOTICE " + message.getReceiver().getNick() + " :" + message.getText());

        message.setSender(user);
    }

    public void send(CTCPMessage message) {
        send("NOTICE " + message.getReceiver().getNick() + " :" + message.getCTCPText());

        message.setSender(user);
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
}

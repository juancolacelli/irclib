package com.colacelli.irclib.connection

import com.colacelli.irclib.actors.Channel
import com.colacelli.irclib.actors.User
import com.colacelli.irclib.connection.connectors.Connector
import com.colacelli.irclib.connection.connectors.PlainConnector
import com.colacelli.irclib.connection.connectors.SSLConnector
import com.colacelli.irclib.connection.listeners.*
import com.colacelli.irclib.messages.*
import kotlin.random.Random

class Connection(val server: Server, val user: User) : Listenable {
    private val listeners = HashMap<Listener.Type, ArrayList<Listener>>()
    private val channels = HashMap<String, Channel>()
    private var connector: Connector = PlainConnector()

    init {
        if (server.ssl) connector = SSLConnector()

        // Use alternative nick if nickname is in use
        addListener(object : OnRawCodeListener {
            override fun rawCode(): Int {
                return Rawable.RawCode.NICKNAME_IN_USE.code
            }

            override fun onRawCode(connection: Connection, message: String, rawCode: Int, args: List<String>) {
                nick("${user.nick}${Random.nextInt(10000)}")
            }
        })

        // Exec OnConnectListeners
        addListener(object : OnRawCodeListener {
            override fun rawCode(): Int {
                return Rawable.RawCode.LOGGED_IN.code
            }

            override fun onRawCode(connection: Connection, message: String, rawCode: Int, args: List<String>) {
                listeners[OnConnectListener.TYPE]?.forEach {
                    if (it is OnConnectListener) it.onConnect(connection, server, user)
                }
            }
        })

        // PING!
        addListener(object : OnServerMessageListener {
            override fun serverMessage(): String {
                return "PING"
            }

            override fun onServerMessage(connection: Connection, message: String, command: String, args: List<String>) {
                send("PONG " + message.substring(5))

                listeners[OnPingListener.TYPE]?.forEach {
                    if (it is OnPingListener) it.onPing(connection)
                }
            }
        })

        // Messages
        addListener(object : OnServerMessageListener {
            override fun serverMessage(): String {
                return "PRIVMSG"
            }

            override fun onServerMessage(connection: Connection, message: String, command: String, args: List<String>) {
                val nickIndex = message.indexOf("!")
                val messageIndex = message.indexOf(":", 1)

                val nick = message.substring(1, nickIndex)
                val login = message.substring(nickIndex + 1, message.indexOf("@"))
                val text = message.substring(messageIndex + 1)

                val user = User(nick, login)

                // Channel message?
                val channel = channels[args[2]]
                if (channel != null) {
                    val channelMessage = ChannelMessage(channel, text, user)
                    listeners[OnChannelMessageListener.TYPE]?.forEach {
                        if (it is OnChannelMessageListener) {
                            it.onChannelMessage(connection, channelMessage)
                        }
                    }
                } else {
                    // CTCP?
                    if (text.startsWith(OnCTCPListener.CTCP_CHARACTER) && text.endsWith(OnCTCPListener.CTCP_CHARACTER)) {
                        val ctcp = text.substring(1, text.length - 1)
                        val ctcpMessage = CTCPMessage(text, ctcp, user, connection.user)

                        listeners[OnCTCPListener.TYPE]?.forEach {
                            if (it is OnCTCPListener) {
                                it.onCTCP(connection, ctcpMessage, args)
                            }
                        }
                    } else {
                        // Private message
                        val privateMessage = PrivateMessage(text, user, connection.user)

                        listeners[OnPrivateMessageListener.TYPE]?.forEach {
                            if (it is OnPrivateMessageListener) {
                                it.onPrivateMessage(connection, privateMessage)
                            }
                        }
                    }
                }
            }
        })

        addListener(object : OnServerMessageListener {
            override fun serverMessage(): String {
                return "JOIN"
            }

            override fun onServerMessage(connection: Connection, message: String, command: String, args: List<String>) {
                val channel = channels[args[2].substring(1)]

                if (channel != null) {
                    val user = User(message.substring(1, message.indexOf("!")))
                    listeners[OnJoinListener.TYPE]?.forEach {
                        if (it is OnJoinListener) {
                            it.onJoin(connection, user, channel)
                        }
                    }
                }
            }
        })

        addListener(object : OnServerMessageListener {
            override fun serverMessage(): String {
                return "PART"
            }

            override fun onServerMessage(connection: Connection, message: String, command: String, args: List<String>) {
                val channel = channels[args[2].substring(1)]

                if (channel != null) {
                    val user = User(message.substring(1, message.indexOf("!")))
                    listeners[OnPartListener.TYPE]?.forEach {
                        if (it is OnPartListener) {
                            it.onPart(connection, user, channel)
                        }
                    }
                }
            }
        })

        addListener(object : OnServerMessageListener {
            override fun serverMessage(): String {
                return "KICK"
            }

            override fun onServerMessage(connection: Connection, message: String, command: String, args: List<String>) {
                val channel = channels[args[2]]

                if (channel != null) {
                    val user = User(args[3])
                    listeners[OnKickListener.TYPE]?.forEach {
                        if (it is OnKickListener) {
                            it.onKick(connection, user, channel)
                        }
                    }
                }
            }
        })

        addListener(object : OnServerMessageListener {
            override fun serverMessage(): String {
                return "MODE"
            }

            override fun onServerMessage(connection: Connection, message: String, command: String, args: List<String>) {
                val channel = channels[args[2]]

                if (channel != null) {
                    listeners[OnChannelModeListener.TYPE]?.forEach {
                        if (it is OnChannelModeListener) {
                            it.onChannelMode(connection, channel, args[3], *args.drop(4).toTypedArray())
                        }
                    }
                }
            }
        })

        addListener(object : OnServerMessageListener {
            override fun serverMessage(): String {
                return "NICK"
            }

            override fun onServerMessage(connection: Connection, message: String, command: String, args: List<String>) {
                val nick = args[2].substring(1)
                val oldNick = message.substring(1, message.indexOf("!"))
                val user = User(nick)
                user.oldNick = oldNick

                listeners[OnNickChangeListener.TYPE]?.forEach {
                    if (it is OnNickChangeListener) {
                        it.onNickChange(connection, user)
                    }
                }
            }
        })
    }

    override fun addListener(listener: Listener) {
        val currentListeners = listeners.getOrDefault(listener.type, ArrayList())
        currentListeners.add(listener)

        listeners[listener.type] = currentListeners
    }

    override fun removeListener(listener: Listener) {
        listeners[listener.type]?.remove(listener)
    }

    fun connect() {
        connector.connect(server, user)

        if (server.password.isNotBlank()) send("PASS ${server.password}")

        nick(user.nick)
        send("USER ${user.login} 8 * : ${user.realName}")

        listen()
    }

    fun disconnect() {
        connector.disconnect()

        listeners[OnDisconnectListener.TYPE]?.forEach {
            if (it is OnDisconnectListener) {
                it.onDisconnect(this, server)
            }
        }
    }

    private fun listen() {
        // FIXME: while (line = connector.listen()) != null doesn't work in Kotlin
        while (true) {
            try {
                val text = connector.listen()
                if (text != null) {
                    if (text.isNotBlank()) {
                        println(text)

                        val words = text.split(" ")

                        // Raw code?
                        val rawCode = try {
                            words[1].toInt()
                        } catch (e: NumberFormatException) {
                            -1
                        }

                        if (rawCode > -1) {
                            listeners[OnRawCodeListener.TYPE]!!.toTypedArray().forEach {
                                if (it is OnRawCodeListener && it.rawCode() == rawCode) {
                                    it.onRawCode(this, text, rawCode, words)
                                }
                            }
                        } else {
                            listeners[OnServerMessageListener.TYPE]!!.toTypedArray().forEach {
                                if (it is OnServerMessageListener) {
                                    for (j in 0..1) {
                                        val serverMessage = words[j].toUpperCase()
                                        if (it.serverMessage() == serverMessage) {
                                            it.onServerMessage(this, text, serverMessage, words)
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Disconnected!
                    disconnect()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                disconnect()
            }
        }
    }

    fun send(text: String) {
        println(text)
        connector.send(text)
    }

    fun send(message: ChannelMessage) {
        message.sender = user
        send("PRIVMSG ${message.channel.name} :${message.text}")
    }

    fun send(message: ChannelNoticeMessage) {
        message.sender = user
        send("NOTICE ${message.channel.name} :${message.text}")
    }

    fun send(message: PrivateMessage) {
        message.sender = user
        send("PRIVMSG ${message.receiver?.nick} :${message.text}")
    }

    fun send(message: PrivateNoticeMessage) {
        message.sender = user
        send("NOTICE ${message.receiver?.nick} :${message.text}")
    }

    fun send(message: CTCPMessage) {
        message.sender = user
        send("NOTICE ${message.receiver?.nick} :$message")
    }

    fun nick(nick: String) {
        user.oldNick = user.nick
        user.nick = nick
        send("NICK $nick")
    }

    fun mode(mode: String) {
        send("MODE ${user.nick} $mode")
    }

    fun mode(channel: Channel, mode: String) {
        send("MODE ${channel.name} $mode")
    }

    fun invite(channel: Channel, user: User) {
        send("INVITE ${user.nick}: ${channel.name}")
    }

    fun kill(user: User, reason: String) {
        send("KILL ${user.nick} $reason")
    }

    fun whois(user: User) {
        send("WHOIS ${user.nick}")
    }

    fun topic(channel: Channel, topic: String) {
        send("TOPIC ${channel.name} :$topic")
    }

    fun kick(channel: Channel, user: User, reason: String) {
        send("KICK ${channel.name} ${user.nick} $reason")
    }

    fun join(channel: Channel) {
        channels[channel.name] = channel
        send("JOIN ${channel.name}")
    }

    fun part(channel: Channel) {
        channels.remove(channel.name)
        send("PART ${channel.name}")
    }
}
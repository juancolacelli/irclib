package com.colacelli.irclib.connection.connectors

import com.colacelli.irclib.actors.User
import com.colacelli.irclib.connection.Server

import java.io.IOException

interface Connector {

    @Throws(IOException::class)
    fun connect(server: Server, user: User)

    @Throws(IOException::class)
    fun disconnect()

    @Throws(IOException::class)
    fun listen(): String?

    @Throws(IOException::class)
    fun send(text: String)

    companion object {
        const val ENTER = "\r\n"
    }
}

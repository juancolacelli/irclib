package com.colacelli.irclib.connection.connectors

import com.colacelli.irclib.actors.User
import com.colacelli.irclib.connection.Server
import com.colacelli.irclib.connection.connectors.Connector.Companion.ENTER
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket

class PlainConnector : Connector {
    private var socket: Socket? = null
    private var reader: BufferedReader? = null
    private var writer: BufferedWriter? = null

    override fun connect(server: Server, user: User) {
        socket = Socket(server.hostname, server.port)
        reader = BufferedReader(InputStreamReader(socket!!.getInputStream()))
        writer = BufferedWriter(OutputStreamWriter(socket!!.getOutputStream()))
    }

    override fun disconnect() {
        reader!!.close()
        writer!!.close()
        socket!!.close()
    }

    override fun listen(): String {
        return reader!!.readLine()
    }

    override fun send(text: String) {
        writer!!.write("$text $ENTER")
        writer!!.flush()
    }
}
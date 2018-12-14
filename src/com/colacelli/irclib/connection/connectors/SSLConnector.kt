package com.colacelli.irclib.connection.connectors

import com.colacelli.irclib.actors.User
import com.colacelli.irclib.connection.Server
import com.colacelli.irclib.connection.connectors.Connector.Companion.ENTER
import java.io.BufferedReader
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory

class SSLConnector : Connector {
    var socket : SSLSocket? = null
    var reader : BufferedReader? = null
    var writer : DataOutputStream? = null

    override fun connect(server: Server, user: User) {
        val socketFactory = SSLSocketFactory.getDefault()
        socket = socketFactory.createSocket(server.hostname, server.port) as SSLSocket?
        writer = DataOutputStream(socket!!.outputStream)
        val dataInputStream = DataInputStream(socket!!.inputStream)
        reader = BufferedReader(InputStreamReader(dataInputStream, StandardCharsets.UTF_8))
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
        writer!!.write(text.toByteArray())
        writer!!.writeBytes(ENTER)
        writer!!.flush()
    }
}
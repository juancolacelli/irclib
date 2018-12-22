package com.colacelli.irclib.connection.listeners

interface Listenable {
    fun addListener(listener: Listener)
    fun removeListener(listener: Listener)
}

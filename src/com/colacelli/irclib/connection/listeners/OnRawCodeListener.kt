package com.colacelli.irclib.connection.listeners

import com.colacelli.irclib.connection.Connection

interface OnRawCodeListener : Listener {
    fun rawCode(): Int
    fun onRawCode(connection: Connection, message: String, rawCode: Int, args: List<String>)

    override val type: Listener.Type
        get() = TYPE

    companion object {
        val TYPE = Listener.Type.RAW_CODE
    }
}

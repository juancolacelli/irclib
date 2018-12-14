package com.colacelli.irclib.messages

import com.colacelli.irclib.actors.User


open class PrivateMessage(text: String, sender: User?, val receiver: User?) :Message(text, sender)

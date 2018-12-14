package com.colacelli.irclib.messages

import com.colacelli.irclib.actors.User

abstract class Message(val text: String, var sender: User?)

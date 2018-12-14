package com.colacelli.irclib.messages

import com.colacelli.irclib.actors.Channel
import com.colacelli.irclib.actors.User

open class ChannelMessage(val channel: Channel, text: String, sender: User?) : Message(text, sender)

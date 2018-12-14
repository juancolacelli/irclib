package com.colacelli.irclib.messages

import com.colacelli.irclib.actors.Channel
import com.colacelli.irclib.actors.User

class ChannelNoticeMessage(channel: Channel, text: String, sender: User?) : ChannelMessage(channel, text, sender) {
}
package com.colacelli.irclib.messages

import com.colacelli.irclib.actors.User

class PrivateNoticeMessage(text: String, sender: User?, receiver: User?) : PrivateMessage(text, sender, receiver)

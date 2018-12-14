package com.colacelli.irclib.actors

class User(var nick: String, val login: String = "", val realName: String = "") {
    var oldNick: String? = ""
}
package com.colacelli.irclib.connection

interface Rawable {
    enum class RawCode(val code: Int) {
        LOGGED_IN(4),
        WHOIS_IDENTIFIED_NICK(307),
        WHOIS_SSL(671),
        WHOIS_END(318),
        ERRONEUS_NICKNAME(432),
        NICKNAME_IN_USE(433),
        JOIN_BANNED(474)
    }
}
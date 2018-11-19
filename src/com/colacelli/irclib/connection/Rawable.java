package com.colacelli.irclib.connection;

public interface Rawable {
    enum RawCode {
        LOGGED_IN(4),
        WHOIS_IDENTIFIED_NICK(307),
        WHOIS_SSL(671),
        WHOIS_END(318),
        NICKNAME_IN_USE(433),
        JOIN_BANNED(474);

        private final int code;

        RawCode(int newCode) {
            code = newCode;
        }

        public int getCode() {
            return code;
        }
    }
}

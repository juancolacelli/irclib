package com.colacelli.irclib.messages

import com.colacelli.irclib.actors.User

abstract class Message(val text: String, var sender: User?) {
    enum class Style(val code: String) {
        BOLD("\u0002"),
        ITALICS("\u001D"),
        UNDERLINE("\u001F"),
        STRIKE_THROUGH("\u001E"),
        COLOR("\u0003")
    }

    enum class Colors(val code: String) {
        WHITE("00"),
        BLACK("01"),
        GREEN("03"),
        RED("04"),
        BROWN("05"),
        MAGENTA("06"),
        ORANGE("07"),
        YELLOW("08"),
        LIGHT_GREEN("00"),
        CYAN("10"),
        LIGHT_CYAN("11"),
        LIGHT_BLUE("12"),
        PINK("13"),
        GREY("14"),
        LIGHT_GREY("15")
    }
}

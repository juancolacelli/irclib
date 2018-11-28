package com.colacelli.irclib.connection.listeners;

import com.colacelli.irclib.connection.Connection;

public interface OnRawCodeListener extends Listener {
    Type TYPE = Type.RAW_CODE;

    int rawCode();
    void onRawCode(Connection connection, String message, int rawCode, String... args);

    @Override
    default Type getType() {
        return TYPE;
    }
}

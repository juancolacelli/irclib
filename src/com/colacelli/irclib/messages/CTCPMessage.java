package com.colacelli.irclib.messages;

import static com.colacelli.irclib.connection.listeners.OnCtcpListener.CTCP_CHARACTER;

public class CTCPMessage extends PrivateMessage {
    protected String command;

    private CTCPMessage(Builder builder) {
        super(builder);
        command = builder.command;
    }

    public String getCommand() {
        return command;
    }

    public String getCTCPText() {
        return CTCP_CHARACTER + command + " " + text + CTCP_CHARACTER;
    }

    public static class Builder extends PrivateMessage.Builder {
        private String command;

        public Builder setCommand(String command) {
            this.command = command.toUpperCase();
            return this;
        }

        public CTCPMessage build() {
            return new CTCPMessage(this);
        }
    }
}

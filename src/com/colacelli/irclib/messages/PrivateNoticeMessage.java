package com.colacelli.irclib.messages;

public class PrivateNoticeMessage extends PrivateMessage {
    private PrivateNoticeMessage(Builder builder) {
        super(builder);
    }

    public static class Builder extends PrivateMessage.Builder {
        public PrivateNoticeMessage build() {
            return new PrivateNoticeMessage(this);
        }
    }
}

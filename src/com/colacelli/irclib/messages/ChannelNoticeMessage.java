package com.colacelli.irclib.messages;

public class ChannelNoticeMessage extends ChannelMessage {
    private ChannelNoticeMessage(Builder builder) {
        super(builder);
    }

    public static class Builder extends ChannelMessage.Builder {
        @Override
        public ChannelNoticeMessage build() {
            return new ChannelNoticeMessage(this);
        }
    }
}

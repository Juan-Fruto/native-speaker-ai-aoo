package com.juanfruto.model;

public class MessageUI {
    private String text;
    private boolean isUserMessage;

    public MessageUI(String text, boolean isUserMessage) {
        this.text = text;
        this.isUserMessage = isUserMessage;
    }

    public String getText() {
        return text;
    }

    public boolean isUserMessage() {
        return isUserMessage;
    }
}


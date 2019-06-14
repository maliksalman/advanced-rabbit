package com.smalik.advancedrabbit.routed;

import java.util.UUID;

public class RoutedMessage {

    private String color;
    private String word;
    private String id;

    public RoutedMessage(String color, String word) {
        this.color = color;
        this.word = word;
        this.id = UUID.randomUUID().toString();
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

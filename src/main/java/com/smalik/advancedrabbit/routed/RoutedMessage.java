package com.smalik.advancedrabbit.routed;

import java.util.UUID;

public class RoutedMessage {

    private String color;
    private String greeting;
    private String id;

    public RoutedMessage(String color, String greeting) {
        this.color = color;
        this.greeting = greeting;
        this.id = UUID.randomUUID().toString();
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

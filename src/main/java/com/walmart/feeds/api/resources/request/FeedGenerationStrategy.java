package com.walmart.feeds.api.resources.request;

import java.util.Arrays;

public enum FeedGenerationStrategy {

    FULL("full"),
    INVENTORY("inventory"),
    PARTIAL("partial");

    private String type;

    FeedGenerationStrategy(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static FeedGenerationStrategy getFromCode(String type) {
        return Arrays.stream(FeedGenerationStrategy.values()).filter(f -> f.getType().equals(type)).findFirst().orElseThrow(() -> new RuntimeException("Feed generation strategy not found for strategy=" + type));
    }
}

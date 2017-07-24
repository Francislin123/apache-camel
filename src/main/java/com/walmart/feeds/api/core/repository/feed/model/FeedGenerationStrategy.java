package com.walmart.feeds.api.core.repository.feed.model;

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
        return Arrays.stream(FeedGenerationStrategy.values()).filter(f -> f.getType().equals(type)).findFirst().orElseThrow(() -> new RuntimeException("FeedTO generation strategy not found for strategy=" + type));
    }
}

package com.walmart.feeds.api.core.repository.feed.model;

import java.util.Arrays;

public enum FeedType {

    FULL("full"),
    INVENTORY("inventory"),
    PARTIAL("partial");

    private String type;

    FeedType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static FeedType getFromCode(String type) {
        // TODO[r0i001q]: throw an specific exception
        return Arrays.stream(FeedType.values()).filter(f -> f.getType().equals(type)).findFirst().orElseThrow(() -> new RuntimeException("FeedType not found for type=" + type));
    }
}

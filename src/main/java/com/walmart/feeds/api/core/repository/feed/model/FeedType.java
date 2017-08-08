package com.walmart.feeds.api.core.repository.feed.model;

import com.walmart.feeds.api.core.exceptions.SystemException;

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
        return Arrays.stream(FeedType.values()).filter(f -> f.getType().equals(type)).findFirst().orElseThrow(() -> new SystemException("FeedType not found for type=" + type));
    }

    @Override
    public String toString() {
        return type;
    }
}

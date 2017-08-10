package com.walmart.feeds.api.core.repository.feed.model;

import com.walmart.feeds.api.core.exceptions.SystemException;

import java.util.Arrays;

public enum FeedNotificationMethod {

    FILE("file"),
    API("api");

    private String type;

    FeedNotificationMethod(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static FeedNotificationMethod getFromCode(String type) {
        return Arrays.stream(FeedNotificationMethod.values()).filter(f -> f.getType().equals(type)).findFirst().orElseThrow(() -> new SystemException("FeedNotificationMethod not found for type=" + type));
    }

    @Override
    public String toString() {
        return type;
    }
}

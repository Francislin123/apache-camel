package com.walmart.feeds.api.core.repository.feed.model;

import com.walmart.feeds.api.core.exceptions.SystemException;

import java.util.Arrays;

public enum FeedNotificationFormat {

    JSON("json"),
    XML("xml"),
    TSV("tsv");

    private String type;

    FeedNotificationFormat(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static FeedNotificationFormat getFromCode(String type) {
        return Arrays.stream(FeedNotificationFormat.values()).filter(f -> f.getType().equals(type)).findFirst().orElseThrow(() -> new SystemException("FeedNotificationFormat not found for type=" + type));
    }

    @Override
    public String toString() {
        return type;
    }
}

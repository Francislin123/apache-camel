package com.walmart.feeds.api.core.repository.blacklist.model;

import com.walmart.feeds.api.core.exceptions.SystemException;

import java.util.Arrays;

/**
 * Created by vn0y942 on 24/08/17.
 */
public enum TaxonomyOwner {

    WALMART("walmart"), PARTNER("partner");

    private String name;

    TaxonomyOwner(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static TaxonomyOwner getFromName(String name) {
        return Arrays.stream(TaxonomyOwner.values()).filter(f -> f.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new SystemException("TaxonomyOwner not found for type=" + name));
    }

    @Override
    public String toString() {
        return this.name;
    }
}

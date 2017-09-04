package com.walmart.feeds.api.core.repository.blacklist.model;

import com.walmart.feeds.api.core.exceptions.SystemException;

import java.util.Arrays;

/**
 * Created by vn0y942 on 24/08/17.
 */
public enum TaxonomyOwner {

    WALMART, PARTNER;

    public static TaxonomyOwner getFromName(String name) {
        return Arrays.stream(TaxonomyOwner.values()).filter(f -> f.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new SystemException("TaxonomyOwner not found for type=" + name));
    }

}

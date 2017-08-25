package com.walmart.feeds.api.core.repository.blacklist;

/**
 * Created by vn0y942 on 24/08/17.
 */
public enum TaxonomyOwner {

    WALMART("Walmart"), PARTNER("Partner");

    private String owner;

    TaxonomyOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return this.owner;
    }
}

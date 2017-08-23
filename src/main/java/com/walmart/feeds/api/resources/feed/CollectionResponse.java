package com.walmart.feeds.api.resources.feed;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;

import java.util.List;

@Builder
@Getter
public class CollectionResponse<T> {

    List<T> result;

    @Tolerate
    public CollectionResponse() {
        //default constructor
    }
}

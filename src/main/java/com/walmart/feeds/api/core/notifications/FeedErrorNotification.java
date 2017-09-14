package com.walmart.feeds.api.core.notifications;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FeedErrorNotification {
    private String partnerSlug;
    private String feedSlug;
    private String message;
}

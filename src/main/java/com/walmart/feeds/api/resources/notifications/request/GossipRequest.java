package com.walmart.feeds.api.resources.notifications.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GossipRequest {

    @NotBlank
    private String feedSlug;
    @NotBlank
    private String partnerSlug;
    @NotBlank
    private String message;

}

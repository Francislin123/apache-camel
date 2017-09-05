package com.walmart.feeds.api.resources.feed.request;

import com.walmart.feeds.api.core.utils.Constants;
import com.walmart.feeds.api.resources.blacklist.request.TaxonomyBlacklistRequest;
import com.walmart.feeds.api.resources.feed.validator.annotation.NotEmptyMapEntry;
import com.walmart.feeds.api.resources.feed.validator.annotation.ValidFeedNotificationUrl;
import com.walmart.feeds.api.resources.feed.validator.annotation.ValidFeedType;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Map;

@Data
@Validated
public class FeedRequest {

    @NotBlank
    @Pattern(regexp = Constants.NO_SPACES_START_END, message = "The name cannot start or ends with whitespace")
    @Size(max = 50)
    private String name;

    @NotBlank
    @ValidFeedType
    private String type;

    @Valid
    @NotNull
    @ValidFeedNotificationUrl
    private FeedNotificationData notification;

    private String taxonomyBlacklist;

    @Valid
    @NotEmptyMapEntry(allowedKeyPattern = "[\\w\\d-_]+", allowedValuePattern = "[\\w\\d\\s-_]+")
    private Map<String, String> utms;

    @NotNull
    private Boolean active;

    @NotBlank
    private String template;

    private Long collectionId;

    @NotBlank
    private String fieldMapping;

    private String taxonomy;

}

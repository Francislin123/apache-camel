package com.walmart.feeds.api.resources.blacklist.request;


import com.walmart.feeds.api.core.utils.Constants;
import com.walmart.feeds.api.resources.feed.validator.annotation.NotEmptyElements;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Builder
@Getter
public class TermsBlacklistRequest {

    @NotBlank
    @Pattern(regexp = Constants.NO_SPACES_START_END, message = "The name cannot start or ends with whitespace")
    private String name;

    @Valid
    @NotEmptyElements
    private Set<String> list;

    @Tolerate
    public TermsBlacklistRequest() {
        // Default constructor
    }
}

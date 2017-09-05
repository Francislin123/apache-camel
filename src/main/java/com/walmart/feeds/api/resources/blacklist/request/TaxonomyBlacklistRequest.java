package com.walmart.feeds.api.resources.blacklist.request;

import com.walmart.feeds.api.core.utils.Constants;
import com.walmart.feeds.api.resources.feed.validator.annotation.NotEmptyElements;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Data
@Validated
public class TaxonomyBlacklistRequest {

    @NotBlank
    @Pattern(regexp = Constants.NO_SPACES_START_END, message = "The name cannot start or ends with whitespace")
    private String name;

    @Valid
    @NotEmptyElements
    private Set<TaxonomyBlacklistMappingRequest> list;

}

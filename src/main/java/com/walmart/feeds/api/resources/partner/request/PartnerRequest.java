package com.walmart.feeds.api.resources.partner.request;

import com.walmart.feeds.api.core.utils.Constants;
import com.walmart.feeds.api.resources.feed.validator.annotation.NotEmptyElements;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
@Getter
public class PartnerRequest {

    @NotBlank
    @Pattern(regexp = Constants.NO_SPACES_START_END, message = "The name cannot start or ends with whitespace")
    @Size(max = 50)
    private String name;

    @Size(max = 255)
    private String description;

    @NotEmptyElements(allowedPattern = "[\\w\\d-\\s_]+")
    private List<String> partnerships;

    private boolean active;

    @Tolerate
    public PartnerRequest() {
        //default constructor
    }
}

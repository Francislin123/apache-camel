package com.walmart.feeds.api.resources.generator.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.walmart.feeds.api.core.utils.Constants;
import com.walmart.feeds.api.resources.serializers.LocalDateTimeDeserializer;
import com.walmart.feeds.api.resources.serializers.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Getter
@Builder
public class GenerationHistoryRequest {

    @NotBlank
    @Pattern(regexp = Constants.NO_SPACES_START_END, message = "The name cannot start or ends with whitespace")
    private String partnerSlug;

    @NotBlank
    @Pattern(regexp = Constants.NO_SPACES_START_END, message = "The name cannot start or ends with whitespace")
    private String feedSlug;

    @NotBlank
    @Pattern(regexp = Constants.NO_SPACES_START_END, message = "The name cannot start or ends with whitespace")
    private String fileName;

    @Min(1)
    private int totalSkus;

    @NotNull
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime generationDate;

    @Tolerate
    public GenerationHistoryRequest() {
        // do nothing
    }
}

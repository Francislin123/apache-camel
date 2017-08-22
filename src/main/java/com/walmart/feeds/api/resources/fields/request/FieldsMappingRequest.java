package com.walmart.feeds.api.resources.fields.request;

import com.walmart.feeds.api.core.repository.fields.model.MappedFieldEntity;
import com.walmart.feeds.api.resources.feed.validator.annotation.NotEmptyElements;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * Created by vn0gshm on 08/08/17.
 */
@Builder
@Getter
public class FieldsMappingRequest {

    @NotBlank
    @Pattern(regexp = "^[^\\s].*", message = "The name cannot start with whitespace")
    private String name;

    @Valid
    @NotEmptyElements
    private List<MappedFieldEntity> mappedFields;

    @Tolerate
    public FieldsMappingRequest() {
    }
}

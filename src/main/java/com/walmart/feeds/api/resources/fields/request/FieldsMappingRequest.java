package com.walmart.feeds.api.resources.fields.request;

import com.walmart.feeds.api.core.repository.fields.model.MappedFieldEntity;
import com.walmart.feeds.api.resources.feed.validator.annotation.NotEmptyElements;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by vn0gshm on 08/08/17.
 */
@Builder
@Getter
public class FieldsMappingRequest {

    @NotBlank
    private String name;

    @Valid
    @NotEmptyElements
    private List<MappedFieldEntity> mappedFields;

    @Tolerate
    public FieldsMappingRequest() {
    }
}
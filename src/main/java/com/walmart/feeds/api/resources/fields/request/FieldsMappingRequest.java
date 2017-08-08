package com.walmart.feeds.api.resources.fields.request;

import com.walmart.feeds.api.core.repository.fields.model.MappedFieldEntity;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * Created by vn0gshm on 08/08/17.
 */
@Builder
@Getter
public class FieldsMappingRequest {

    @NotBlank
    private String name;

    @NotEmpty
    private List<MappedFieldEntity> mappedFields;
}

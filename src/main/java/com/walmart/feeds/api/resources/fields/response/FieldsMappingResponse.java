package com.walmart.feeds.api.resources.fields.response;

import com.walmart.feeds.api.core.repository.fields.model.MappedFieldEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Created by vn0gshm on 09/08/17.
 */
@Builder
@Getter
public class FieldsMappingResponse {

    private String name;

    private String slug;

    private List<MappedFieldEntity> mappedFields;
}

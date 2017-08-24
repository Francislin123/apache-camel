package com.walmart.feeds.api.resources.taxonomy.request;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
public class UploadTaxonomyMappingTO {

    private String slug;
    private String name;
    private String partnerSlug;
    private MultipartFile taxonomyMapping;

}

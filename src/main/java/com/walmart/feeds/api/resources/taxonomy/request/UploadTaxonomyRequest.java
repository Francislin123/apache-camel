package com.walmart.feeds.api.resources.taxonomy.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UploadTaxonomyRequest {

    @NotNull
    private MultipartFile file;

    @NotBlank
    @Pattern(regexp = "^[\\S].*\\S", message = "The name cannot start or ends with whitespace")
    @Size(max = 50)
    private String name;

}

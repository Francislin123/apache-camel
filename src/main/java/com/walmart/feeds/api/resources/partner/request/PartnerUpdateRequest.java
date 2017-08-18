package com.walmart.feeds.api.resources.partner.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Data
public class PartnerUpdateRequest {

    @NotBlank
    @Pattern(regexp = "^[^\\s].*", message = "The name cannot start with whitespace")
    private String name;

    private String description;

    @NotEmpty
    private List<String> partnerships = new ArrayList<>();

}

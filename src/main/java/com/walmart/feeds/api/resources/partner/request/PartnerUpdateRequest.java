package com.walmart.feeds.api.resources.partner.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class PartnerUpdateRequest {

    @NotNull
    private String name;

    private String description;

    @NotEmpty
    private List<String> partnerships = new ArrayList<>();

}

package com.walmart.feeds.api.resources.partner.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class PartnerRequest {

    @NotNull
    private String name;

    @NotNull
    private String reference;

    private String description;

    @NotEmpty
    private List<String> partnerships = new ArrayList<>();

    private boolean active;

}

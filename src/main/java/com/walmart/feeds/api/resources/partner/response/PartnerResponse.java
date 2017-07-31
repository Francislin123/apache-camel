package com.walmart.feeds.api.resources.partner.response;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class PartnerResponse {

    private String name;

    private String reference;

    private String description;

    private List<String> partnerships;

    private boolean active;

}

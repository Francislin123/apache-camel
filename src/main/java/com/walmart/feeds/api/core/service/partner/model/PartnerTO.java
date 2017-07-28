package com.walmart.feeds.api.core.service.partner.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class PartnerTO {

    private String name;

    private String reference;

    private String description;

    private List<String> partnership = new ArrayList<>();

    private boolean active;

}

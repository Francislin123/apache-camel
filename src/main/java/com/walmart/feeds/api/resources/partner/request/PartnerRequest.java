package com.walmart.feeds.api.resources.partner;

import lombok.Data;

import java.util.List;

@Data
public class PartnerRequest {

    private String name;

    private String reference;

    private String description;

    private List<String> partnerships;

    private boolean active;

}

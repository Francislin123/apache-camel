package com.walmart.feeds.api.core.service.partner.model;

import com.walmart.feeds.api.core.repository.partner.model.Partnership;
import lombok.Data;

import java.util.Calendar;
import java.util.List;

@Data
public class PartnerTO {

    private Long id;

    private String name;

    private String reference;

    private String description;

    private List<Partnership> partnerships;

    private Calendar creationDate;

    private Calendar updateDate;

    private boolean active;

}

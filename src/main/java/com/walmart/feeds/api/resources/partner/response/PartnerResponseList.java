package com.walmart.feeds.api.resources.partner.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PartnerResponseList {

    private List<PartnerResponse> partners;

    public PartnerResponseList(List<PartnerResponse> partnerResponseList) {
        this.partners = partnerResponseList;
    }
}

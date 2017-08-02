package com.walmart.feeds.api.resources.partner.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Tolerate;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class PartnerRequest {

    @NotNull
    private String name;

    private String description;

    @NotEmpty
    private List<String> partnerships = new ArrayList<>();

    private boolean active;

    @Tolerate
    public PartnerRequest() {
    }
}

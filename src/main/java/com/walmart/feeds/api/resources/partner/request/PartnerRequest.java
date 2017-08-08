package com.walmart.feeds.api.resources.partner.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Tolerate;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class PartnerRequest {

    @NotBlank
    @Size(max = 50)
    private String name;

    @Size(max = 255)
    private String description;

    @NotEmpty
    private List<String> partnerships;

    private boolean active;

    @Tolerate
    public PartnerRequest() {
    }
}

package com.walmart.feeds.api.client.tags;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Tolerate;

import java.util.Date;

/**
 * Created by vn0gshm on 24/08/17.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TagAdminCollection {
    private Integer id;
    private Integer version;
    private String name;
    private String domain;
    private String status;
    private Date expiresAt;

    @Tolerate
    public TagAdminCollection() {}

    @Builder
    private TagAdminCollection(Integer id, Integer version, String name, String domain, String status, Date expiresAt) {
        this.id = id;
        this.version = version;
        this.name = name;
        this.domain = domain;
        this.status = status;
        this.expiresAt = expiresAt;
    }
}
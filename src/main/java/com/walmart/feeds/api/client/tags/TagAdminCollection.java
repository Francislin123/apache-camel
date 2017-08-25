package com.walmart.feeds.api.client.tags;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
/**
 * Created by vn0gshm on 24/08/17.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TagAdminCollection
{
    private Integer id;
    private Integer version;
    private String name;
    private String domain;
    private String status;
    private Integer expiresAt;
}
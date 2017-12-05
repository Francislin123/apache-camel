package com.walmart.feeds.api.client.categoryCollection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryClient {

    private Integer id;
    private String name;
    private boolean status;
    private Integer idDepartment;
    private CategoryClient parent;
    private boolean flagMenu;
    private long order;

    @Tolerate
    public CategoryClient() {
        //do nothing
    }

    @Builder
    public CategoryClient(Integer id, String name, boolean status, Integer idDepartment, CategoryClient parent, boolean flagMenu, long order) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.idDepartment = idDepartment;
        this.parent = parent;
        this.flagMenu = flagMenu;
        this.order = order;
    }
}

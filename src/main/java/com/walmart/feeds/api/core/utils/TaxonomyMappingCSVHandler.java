package com.walmart.feeds.api.core.utils;

import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;

import java.io.*;


public class TaxonomyMappingCSVHandler {

    public static final String DEFAULT_SEPARATOR = ";";

    public static StringBuilder createGenericHeader(){
        StringBuilder sb = new StringBuilder();
        sb.append("PARTNER_CS_ID");
        sb.append(DEFAULT_SEPARATOR);
        sb.append("PARTNER_TAXONOMY");
        sb.append(DEFAULT_SEPARATOR);
        sb.append("WALMART_TAXONOMY");
        sb.append(System.lineSeparator());
        return sb;
    }

    public static void returnFileBody(PartnerTaxonomyEntity partnerTaxonomyEntity, StringBuilder sb){
        partnerTaxonomyEntity.getTaxonomyMappings().forEach(assoc -> {
            sb.append(assoc.getPartnerPathId());
            sb.append(DEFAULT_SEPARATOR);
            sb.append(assoc.getPartnerPath());
            sb.append(DEFAULT_SEPARATOR);
            sb.append(assoc.getWalmartPath());
            sb.append(System.lineSeparator());
        });
    }
}

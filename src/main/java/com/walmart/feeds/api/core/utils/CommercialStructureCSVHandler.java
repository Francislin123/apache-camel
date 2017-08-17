package com.walmart.feeds.api.core.utils;

import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureEntity;

import java.io.*;


public class CommercialStructureCSVHandler {

    public static final String DEFAULT_SEPARATOR = ";";

    public static File createCSVFile(CommercialStructureEntity commercialStructureEntity) throws IOException {

        if(commercialStructureEntity.getAssociationEntityList().isEmpty())
            return null;

        File returnFile = new File(commercialStructureEntity.getArchiveName() + ".csv");
        FileOutputStream fop = new FileOutputStream(returnFile);
        StringBuilder builder = createGenericHeader();
        returnFileBody(commercialStructureEntity, builder);
        fop.write(builder.toString().getBytes());
        fop.flush();
        fop.close();

        return returnFile;
    }

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

    public static void returnFileBody(CommercialStructureEntity commercialStructureEntity, StringBuilder sb){
        commercialStructureEntity.getAssociationEntityList().forEach( assoc -> {
            sb.append(assoc.getStructurePartnerId());
            sb.append(DEFAULT_SEPARATOR);
            sb.append(assoc.getPartnerTaxonomy());
            sb.append(DEFAULT_SEPARATOR);
            sb.append(assoc.getWalmartTaxonomy());
            sb.append(System.lineSeparator());
        });
    }
}

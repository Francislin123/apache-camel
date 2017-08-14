package com.walmart.feeds.api.resources.camel;

/**
 * Created by vn0y942 on 09/08/17.
 */
import lombok.Data;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@CsvRecord(separator = ";", skipFirstLine = true)
@Data
public class CommercialStructureBindy {

    @DataField(pos = 1, required = true , trim = true)
    private String structurePartnerId;
    @DataField(pos = 2, required = true, trim = true)
    private String partnerTaxonomy;
    @DataField(pos = 3, required = true, trim = true)
    private String walmartTaxonomy;
}

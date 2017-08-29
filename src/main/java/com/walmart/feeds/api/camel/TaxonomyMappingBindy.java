package com.walmart.feeds.api.camel;

/**
 * Created by vn0y942 on 09/08/17.
 */
import lombok.Data;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@CsvRecord(separator = ";", skipFirstLine = true)
@Data
public class TaxonomyMappingBindy {

    @DataField(pos = 1, columnName = "partner_taxonomy_id", trim = true)
    private String structurePartnerId;
    @DataField(pos = 2, columnName = "partner_path", trim = true)
    private String partnerTaxonomy;
    @DataField(pos = 3, columnName = "wm_path", trim = true)
    private String walmartTaxonomy;
}

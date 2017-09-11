package com.walmart.feeds.api.resources.taxonomy.response;

import com.walmart.feeds.api.core.repository.taxonomy.model.ImportStatus;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UploadTaxonomyResponse {

    private ImportStatus status;
    private TaxonomyReportResponse report;

}

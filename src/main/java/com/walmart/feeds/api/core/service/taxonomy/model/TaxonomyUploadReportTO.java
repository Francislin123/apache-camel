package com.walmart.feeds.api.core.service.taxonomy.model;

import com.walmart.feeds.api.core.repository.taxonomy.model.ImportStatus;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import com.walmart.feeds.api.core.repository.taxonomy.model.TaxonomyMappingEntity;
import lombok.Data;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;

import java.util.List;

@Data
public class TaxonomyUploadReportTO {

    private ImportStatus status;
    private SimpleKeycloakAccount authenticationToken;
    private Integer itemsImported;
    private List<TaxonomyMappingEntity> taxonomiesToRemove;
    private List<TaxonomyMappingEntity> taxonomiesToInsert;
    private PartnerTaxonomyEntity entityToSave;
}

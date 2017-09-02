package com.walmart.feeds.api.core.service.blacklist.taxonomy.validation;

import com.walmart.feeds.api.core.exceptions.UserException;
import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyBlacklistEntity;
import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyOwner;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;

import java.util.*;
import java.util.stream.Collectors;

public class TaxonomyBlacklistPartnerValidator {

    /**
     *
     * @param taxonomyEntity
     * @param blacklist
     * @return list containing non matched taxonomies
     */
    public static void validatePartnerTaxonomiesOnBlacklist(TaxonomyBlacklistEntity blacklist, PartnerTaxonomyEntity taxonomyEntity) {

        List<String> nonMachedTaxonomies = new ArrayList<>();

        if (blacklist == null || taxonomyEntity == null) {
            return;
        }

        List<String> partnerTaxonomies = filterPartnerTaxonomies(blacklist);


        // when not exists partner taxonomies to validate, then the result is true because there is no case of failure
        // the if statement avoid more processing, e.g. collections manipulation
        if (partnerTaxonomies.isEmpty()) {
            return;
        }

        final List<String> partnerMappings = taxonomyEntity.getTaxonomyMappings().stream()
                .map(m -> m.getPartnerPath())
                .collect(Collectors.toList());

        nonMachedTaxonomies = partnerTaxonomies.stream()
                .filter(t -> !partnerMappings.contains(t))
                .collect(Collectors.toList());


        if (!nonMachedTaxonomies.isEmpty()) {
            throw new UserException("The partner taxonomies on blacklist were not recognized with the taxonomies saved previously from CSV file: " + nonMachedTaxonomies);
        }

    }

    private static List<String> filterPartnerTaxonomies(TaxonomyBlacklistEntity blacklist) {
        List<String> partnerTaxonomies = new ArrayList<>();

        blacklist.getList().forEach(m -> {
            if (m.getOwner() == TaxonomyOwner.PARTNER) {
                partnerTaxonomies.add(m.getTaxonomy());
            }
        });
        return partnerTaxonomies;
    }

}

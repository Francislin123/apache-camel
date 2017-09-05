package com.walmart.feeds.api.resources.feed.validator;

import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyOwner;
import com.walmart.feeds.api.resources.feed.validator.annotation.ValidTaxonomyOwner;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class TaxonomyOwnerValidator implements ConstraintValidator<ValidTaxonomyOwner, String> {
    @Override
    public void initialize(ValidTaxonomyOwner validTaxonomyOwner) {
        //do-nothing
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        ((ConstraintValidatorContextImpl) constraintValidatorContext).addExpressionVariable("taxonomyOwnerPossibleValues", Arrays.asList(TaxonomyOwner.values()));
        return Arrays.asList(TaxonomyOwner.values()).stream().anyMatch(f -> f.name().equalsIgnoreCase(value));
    }
}

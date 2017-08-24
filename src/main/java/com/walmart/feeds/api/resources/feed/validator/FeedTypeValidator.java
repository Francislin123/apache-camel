package com.walmart.feeds.api.resources.feed.validator;

import com.walmart.feeds.api.core.repository.feed.model.FeedType;
import com.walmart.feeds.api.resources.feed.validator.annotation.ValidFeedType;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class FeedTypeValidator implements ConstraintValidator<ValidFeedType, String> {

    @Override
    public void initialize(ValidFeedType constraintAnnotation) {
        //do-nothing
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        ((ConstraintValidatorContextImpl) context).addExpressionVariable("feedTypePossibleValues", Arrays.asList(FeedType.values()));
        return Arrays.asList(FeedType.values()).stream().anyMatch(f -> f.getType().equals(value));
    }
}

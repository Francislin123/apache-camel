package com.walmart.feeds.api.resources.feed;

import com.walmart.feeds.api.core.repository.feed.model.FeedType;
import com.walmart.feeds.api.resources.feed.validator.annotation.ValidFeedType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class FeedTypeValidator implements ConstraintValidator<ValidFeedType, String> {

    @Override
    public void initialize(ValidFeedType constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.asList(FeedType.values()).stream().anyMatch(f -> f.getType().equals(value));

    }
}

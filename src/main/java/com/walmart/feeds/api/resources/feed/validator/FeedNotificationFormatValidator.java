package com.walmart.feeds.api.resources.feed.validator;

import com.walmart.feeds.api.core.repository.feed.model.FeedNotificationFormat;
import com.walmart.feeds.api.resources.feed.validator.annotation.ValidFeedNotificationFormat;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class FeedNotificationFormatValidator implements ConstraintValidator<ValidFeedNotificationFormat, String> {
    @Override
    public void initialize(ValidFeedNotificationFormat constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        ((ConstraintValidatorContextImpl) context).addExpressionVariable("feedNotificationFormatPossibleValues", Arrays.asList(FeedNotificationFormat.values()));
        return Arrays.asList(FeedNotificationFormat.values()).stream().anyMatch(f -> f.getType().equals(value));
    }
}

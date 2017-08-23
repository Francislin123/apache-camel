package com.walmart.feeds.api.resources.feed.validator;

import com.walmart.feeds.api.core.repository.feed.model.FeedNotificationMethod;
import com.walmart.feeds.api.resources.feed.validator.annotation.ValidFeedNotificationMethod;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class FeedNotificationMethodValidator implements ConstraintValidator<ValidFeedNotificationMethod, String> {

    @Override
    public void initialize(ValidFeedNotificationMethod constraintAnnotation) {
        //do-nothing
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        ((ConstraintValidatorContextImpl) context).addExpressionVariable("feedNotificationMethodPossibleValues", Arrays.asList(FeedNotificationMethod.values()));
        return Arrays.asList(FeedNotificationMethod.values()).stream().anyMatch(f -> f.getType().equals(value));
    }
}

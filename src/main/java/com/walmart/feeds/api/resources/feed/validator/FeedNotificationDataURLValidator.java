package com.walmart.feeds.api.resources.feed.validator;

import com.walmart.feeds.api.core.repository.feed.model.FeedType;
import com.walmart.feeds.api.resources.feed.request.FeedNotificationData;
import com.walmart.feeds.api.resources.feed.validator.annotation.ValidFeedNotificationUrl;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class FeedNotificationDataURLValidator implements ConstraintValidator<ValidFeedNotificationUrl, FeedNotificationData> {

    @Override
    public void initialize(ValidFeedNotificationUrl constraintAnnotation) {

    }

    @Override
    public boolean isValid(FeedNotificationData value, ConstraintValidatorContext context) {

        ((ConstraintValidatorContextImpl) context).addExpressionVariable("apiType", "api");

        if ("api".equals(value.getMethod())) {
            return value.getUrl() != null && !value.getUrl().isEmpty();
        }

        return true;

    }
}

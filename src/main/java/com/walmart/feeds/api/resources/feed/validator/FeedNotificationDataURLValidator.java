package com.walmart.feeds.api.resources.feed.validator;

import com.walmart.feeds.api.resources.feed.request.FeedNotificationData;
import com.walmart.feeds.api.resources.feed.validator.annotation.ValidFeedNotificationUrl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FeedNotificationDataURLValidator implements ConstraintValidator<ValidFeedNotificationUrl, FeedNotificationData> {

    @Override
    public void initialize(ValidFeedNotificationUrl constraintAnnotation) {

    }

    @Override
    public boolean isValid(FeedNotificationData value, ConstraintValidatorContext context) {

        if ("api".equals(value.getMethod())) {
            return value.getUrl() != null && !value.getUrl().isEmpty();
        }

        return true;

    }
}

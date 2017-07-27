package com.walmart.feeds.api.resources.feed.validator.annotation;

import com.walmart.feeds.api.resources.feed.validator.FeedNotificationDataURLValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FeedNotificationDataURLValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidFeedNotificationUrl {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

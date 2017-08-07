package com.walmart.feeds.api.resources.feed.validator.annotation;

import com.walmart.feeds.api.resources.feed.validator.FeedNotificationFormatValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FeedNotificationFormatValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidFeedNotificationFormat {

    String message() default "{com.walmart.validation.constraints.ValidFeedNotificationFormat.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

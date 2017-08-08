package com.walmart.feeds.api.resources.feed.validator.annotation;

import com.walmart.feeds.api.resources.feed.validator.FeedNotificationMethodValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FeedNotificationMethodValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidFeedNotificationMethod {

    String message() default "{com.walmart.validation.constraints.ValidFeedNotificationMethod.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

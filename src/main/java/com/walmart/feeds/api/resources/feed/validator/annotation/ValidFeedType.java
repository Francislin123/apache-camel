package com.walmart.feeds.api.resources.feed.validator.annotation;

import com.walmart.feeds.api.resources.feed.FeedTypeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FeedTypeValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@NotNull(message = "FeedType cannot be null")
public @interface ValidFeedType {

    String message() default "{com.example.beanvalidationcustomconstraint.DayOfWeek.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

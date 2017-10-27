package com.walmart.feeds.api.resources.feed.validator.annotation;

import com.walmart.feeds.api.resources.feed.validator.CronValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CronValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CronPatternValidator {
    String message() default "{com.walmart.validation.constraints.CronValidator.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

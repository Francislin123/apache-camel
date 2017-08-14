package com.walmart.feeds.api.resources.fields.validator.annotation;

import com.walmart.feeds.api.resources.feed.validator.annotation.NotEmptyElements;
import com.walmart.feeds.api.resources.fields.validator.WalmartFieldsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = WalmartFieldsValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@NotEmptyElements
public @interface ValidWalmartFields {

    String message() default "{com.walmart.validation.constraints.ValidWalmartFields.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

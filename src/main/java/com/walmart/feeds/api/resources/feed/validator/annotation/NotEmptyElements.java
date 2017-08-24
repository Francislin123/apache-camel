package com.walmart.feeds.api.resources.feed.validator.annotation;

import com.walmart.feeds.api.resources.feed.validator.NotEmptyElementsValidator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotEmptyElementsValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@NotEmpty
public @interface NotEmptyElements {

    String message() default "{com.walmart.validation.constraints.NotEmptyElements.message}";

    String allowedPattern() default ".*";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

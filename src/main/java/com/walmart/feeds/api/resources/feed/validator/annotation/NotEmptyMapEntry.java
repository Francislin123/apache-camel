package com.walmart.feeds.api.resources.feed.validator.annotation;

import com.walmart.feeds.api.resources.feed.validator.NotEmptyMapValidator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotEmptyMapValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@NotEmpty
public @interface NotEmptyMapEntry {

    String message() default "{com.walmart.validation.constraints.NotEmptyMapEntry.message}";

    String allowedKeyPattern() default ".*";

    String allowedValuePattern() default ".*";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

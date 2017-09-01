package com.walmart.feeds.api.resources.feed.validator.annotation;

import com.walmart.feeds.api.resources.feed.validator.TaxonomyOwnerValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TaxonomyOwnerValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@NotNull(message = "TaxonomyOwner cannot be null")
public @interface ValidTaxonomyOwner {

    String message() default "{com.walmart.validation.constraints.ValidTaxonomyOwner.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

package com.walmart.feeds.api.resources.feed.validator;

import com.walmart.feeds.api.resources.feed.validator.annotation.ValidSlug;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.regex.Pattern;

public class SlugValidator implements ConstraintValidator<ValidSlug, String> {

    private static final String PATTERN = "[a-z0-9]*-?[a-z0-9]*";

    @Override
    public void initialize(ValidSlug validSlug) {
    }

    @Override
    public boolean isValid(@NotNull @Min(4) @Max(50) String slug, ConstraintValidatorContext constraintValidatorContext) {
        return Pattern.compile(PATTERN).matcher(slug).matches();
    }
}

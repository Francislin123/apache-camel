package com.walmart.feeds.api.resources.feed.validator;

import com.walmart.feeds.api.resources.feed.validator.annotation.NotEmptyMapEntry;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

public class NotEmptyMapValidator implements ConstraintValidator<NotEmptyMapEntry, Map<?, String>> {

    @Override
    public void initialize(NotEmptyMapEntry constraintAnnotation) {

    }

    @Override
    public boolean isValid(Map<?, String> map, ConstraintValidatorContext context) {
        return map.values().stream().filter(o -> o == null || StringUtils.isEmpty(o.trim()) || "null" .equals(o)).count() == 0;
    }
}

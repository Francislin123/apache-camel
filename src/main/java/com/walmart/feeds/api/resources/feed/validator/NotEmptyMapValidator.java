package com.walmart.feeds.api.resources.feed.validator;

import com.walmart.feeds.api.resources.feed.validator.annotation.NotEmptyMapEntry;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;
import java.util.Set;

public class NotEmptyMapValidator implements ConstraintValidator<NotEmptyMapEntry, Map<?, ?>> {

    private String allowedKeyPattern;
    private String allowedValuePattern;

    @Override
    public void initialize(NotEmptyMapEntry constraintAnnotation) {
        allowedKeyPattern = constraintAnnotation.allowedKeyPattern();
        allowedValuePattern = constraintAnnotation.allowedValuePattern();
    }

    @Override
    public boolean isValid(Map<?, ?> map, ConstraintValidatorContext context) {

        ((ConstraintValidatorContextImpl) context).addExpressionVariable("regexKeyPattern", allowedKeyPattern);
        ((ConstraintValidatorContextImpl) context).addExpressionVariable("regexValuePattern", allowedValuePattern);

        if (map == null) {
            return false;
        }

        Set<?> keySet = map.keySet();

        long keySetErrorCount = keySet.stream().filter(k -> {
            if (k instanceof String) {
                return !ValidatorUtils.isValid((String) k, allowedKeyPattern, "null");
            }
            return k == null;
        }).count();

        long valuesErrorCount = map.values().stream().filter(v -> {
            if (v instanceof String) {
                return !ValidatorUtils.isValid((String) v, allowedValuePattern, "null");
            }
            return v == null;
        }).count();

        return keySetErrorCount == 0 && valuesErrorCount == 0;
    }

}

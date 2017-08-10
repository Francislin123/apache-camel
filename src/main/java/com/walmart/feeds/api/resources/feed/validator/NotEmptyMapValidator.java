package com.walmart.feeds.api.resources.feed.validator;

import com.walmart.feeds.api.resources.feed.validator.annotation.NotEmptyMapEntry;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.springframework.util.StringUtils;

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

        Set<?> keySet = map.keySet();

        long keySetErrorCount = keySet.stream().filter(k -> {
            if (k instanceof String) {
                if (allowedKeyPattern != null && !allowedKeyPattern.isEmpty()) {
                    String value = (String) k;
                    return !value.matches(allowedKeyPattern) || StringUtils.isEmpty(value.trim()) || "null".equals(value.toLowerCase());
                }
            }
            return k == null;
        }).count();

        long valuesErrorCount = map.values().stream().filter(v -> {
            if (v instanceof String) {
                if (allowedValuePattern != null && !allowedValuePattern.isEmpty()) {
                    String value = (String) v;
                    return !value.matches(allowedValuePattern) || StringUtils.isEmpty(value.trim()) || "null".equals(value.toLowerCase());
                }
            }
            return v == null;
        }).count();

        return keySetErrorCount == 0 && valuesErrorCount == 0;
    }
}

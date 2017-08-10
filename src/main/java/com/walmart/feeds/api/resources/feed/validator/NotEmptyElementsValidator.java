package com.walmart.feeds.api.resources.feed.validator;

import com.walmart.feeds.api.resources.feed.validator.annotation.NotEmptyElements;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

public class NotEmptyElementsValidator implements ConstraintValidator<NotEmptyElements, Collection<?>> {

    private String allowedPattern;

    @Override
    public void initialize(NotEmptyElements notEmptyElements) {
        allowedPattern = notEmptyElements.allowedPattern();
    }

    @Override
    public boolean isValid(Collection<?> o, ConstraintValidatorContext constraintValidatorContext) {

        ((ConstraintValidatorContextImpl) constraintValidatorContext).addExpressionVariable("regexPattern", allowedPattern);

        return o != null && o.stream().filter(a -> {

            if (a instanceof String) {
                if (allowedPattern != null && !allowedPattern.isEmpty()) {
                    String value = (String) a;
                    return !value.matches(allowedPattern) || StringUtils.isEmpty(value.trim());
                }
            }

            return a == null;

        }).count() == 0;
    }

}



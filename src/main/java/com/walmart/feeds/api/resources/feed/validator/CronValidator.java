package com.walmart.feeds.api.resources.feed.validator;

import com.walmart.feeds.api.resources.feed.validator.annotation.CronPatternValidator;
import org.quartz.CronExpression;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CronValidator implements ConstraintValidator<CronPatternValidator, String> {
    @Override
    public void initialize(CronPatternValidator cronPatternValidator) {

    }

    @Override
    public boolean isValid(String cronPattern, ConstraintValidatorContext constraintValidatorContext) {

        return StringUtils.isEmpty(cronPattern) || CronExpression.isValidExpression(cronPattern);

    }
}

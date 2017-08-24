package com.walmart.feeds.api.resources.feed.validator;

import org.springframework.util.StringUtils;

import java.util.Arrays;

public class ValidatorUtils {

    private ValidatorUtils() {
        //default constructor
    }

    public static boolean isValid(String value, String regexPattern, String... notAllowedValues) {

        if (value == null || StringUtils.isEmpty(value.trim())) {
            return false;
        }

        if (notAllowedValues != null && notAllowedValues.length != 0) {
            return !Arrays.asList(notAllowedValues).contains(value);
        }

        return (regexPattern == null || regexPattern.isEmpty()) || !value.matches(regexPattern);

    }

}

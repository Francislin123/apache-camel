package com.walmart.feeds.api.resources.feed.validator;

import org.junit.Test;

import static org.junit.Assert.*;

public class ValidatorUtilsTest {

    @Test
    public void validateWhenIsNull() {
        assertEquals(false, ValidatorUtils.isValid(null, null));
    }

    @Test
    public void validateWhenStringIsEmpty() {
        assertEquals(false, ValidatorUtils.isValid("", null));
    }

    @Test
    public void validateWhenIsBlank() {
        assertEquals(false, ValidatorUtils.isValid("    ", null));
    }

    @Test
    public void validateWhenDoNotMatchPattern() {
        assertEquals(false, ValidatorUtils.isValid("123", "\\w+"));
    }

    @Test
    public void validateWhenMatchPatternButIsNotAllowed() {
        assertEquals(false, ValidatorUtils.isValid("null", "\\w+", "null"));
    }

    @Test
    public void validateWhenIsValid() {
        assertEquals(true, ValidatorUtils.isValid("teste", "\\w+", "null"));
    }

}
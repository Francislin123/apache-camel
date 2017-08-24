package com.walmart.feeds.api.unit.resources.feed.validator;

import com.walmart.feeds.api.resources.feed.validator.ValidatorUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
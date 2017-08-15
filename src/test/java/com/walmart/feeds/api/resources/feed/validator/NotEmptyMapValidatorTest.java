package com.walmart.feeds.api.resources.feed.validator;

import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class NotEmptyMapValidatorTest {

    private NotEmptyMapValidator validator = new NotEmptyMapValidator();

    @Test
    public void validateWhenIsNull() {
        assertEquals(false, validator.isValid(null, mock(ConstraintValidatorContextImpl.class)));
    }

    @Test
    public void validateWhenIsEmpty() {
        assertEquals(true, validator.isValid(new HashMap<>(), mock(ConstraintValidatorContextImpl.class)));
    }

    @Test
    public void validateWhenKeyIsNull() {
        Map<?, String> map = new HashMap<>();
        map.put(null, "teste123");
        assertEquals(false, validator.isValid(map, mock(ConstraintValidatorContextImpl.class)));
    }

    @Test
    public void validateWhenKeyIsEmpty() {
        Map<String, String> map = new HashMap<>();
        map.put("", "teste123");
        assertEquals(false, validator.isValid(map, mock(ConstraintValidatorContextImpl.class)));
    }

    @Test
    public void validateWhenKeyIsBlank() {
        Map<String, String> map = new HashMap<>();
        map.put("    ", "teste123");
        assertEquals(false, validator.isValid(map, mock(ConstraintValidatorContextImpl.class)));
    }

    @Test
    public void validateWhenKeyIsValidButValueIsNull() {
        Map<String, ?> map = new HashMap<>();
        map.put("teste123", null);
        assertEquals(false, validator.isValid(map, mock(ConstraintValidatorContextImpl.class)));
    }

    @Test
    public void validateWhenKeyIsValidButValueIsEmpty() {
        Map<String, String> map = new HashMap<>();
        map.put("teste123", "");
        assertEquals(false, validator.isValid(map, mock(ConstraintValidatorContextImpl.class)));
    }

    @Test
    public void validateWhenKeyIsValidButValueIsBlank() {
        Map<String, String> map = new HashMap<>();
        map.put("teste123", "     ");
        assertEquals(false, validator.isValid(map, mock(ConstraintValidatorContextImpl.class)));
    }

    @Test
    public void validateWhenKeyAndValueIsValid() {
        Map<String, String> map = new HashMap<>();
        map.put("sucesso", "sucesso");
        assertEquals(true, validator.isValid(map, mock(ConstraintValidatorContextImpl.class)));
    }

}
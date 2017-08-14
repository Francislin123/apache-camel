package com.walmart.feeds.api.resources.feed.validator;

import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class NotEmptyElementsValidatorTest {

    private NotEmptyElementsValidator validator = new NotEmptyElementsValidator();

    @Test
    public void validateWhenListIsNull() {
        boolean isValid = validator.isValid(null, mock(ConstraintValidatorContextImpl.class));
        assertEquals(false, isValid);
    }

    @Test
    public void validateWhenListIsEmpty() {
        boolean isValid = validator.isValid(Collections.emptyList(), mock(ConstraintValidatorContextImpl.class));
        assertEquals(true, isValid);
    }

    @Test
    public void validateWhenListContainsStringBlankElements() {
        boolean isValid = validator.isValid(Arrays.asList("    "), mock(ConstraintValidatorContextImpl.class));
        assertEquals(false, isValid);
    }

    @Test
    public void validateWhenListContainsStringEmptyElements() {
        boolean isValid = validator.isValid(Arrays.asList(""), mock(ConstraintValidatorContextImpl.class));
        assertEquals(false, isValid);
    }

    @Test
    public void validateWhenListContainsNullElements() {
        List<Object> a = new LinkedList<>();
        a.add(null);
        boolean isValid = validator.isValid(a, mock(ConstraintValidatorContextImpl.class));
        assertEquals(false, isValid);
    }

    @Test
    public void validateWhenListIsValid() {
        List<Object> a = new LinkedList<>();
        a.add("teste");
        boolean isValid = validator.isValid(a, mock(ConstraintValidatorContextImpl.class));
        assertEquals(true, isValid);
    }

}
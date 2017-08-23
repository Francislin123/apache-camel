package com.walmart.feeds.api.unit.resources.feed.validator;

import com.walmart.feeds.api.resources.feed.validator.FeedTypeValidator;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class FeedTypeValidatorTest {

    private FeedTypeValidator validator = new FeedTypeValidator();

    @Test
    public void validateWhenTypeIsInvalid(){
        boolean isValid = validator.isValid("teste123", Mockito.mock(ConstraintValidatorContextImpl.class));
        assertEquals(false, isValid);
    }

    @Test
    public void validateWhenTypeIsNull(){
        boolean isValid = validator.isValid(null, Mockito.mock(ConstraintValidatorContextImpl.class));
        assertEquals(false, isValid);
    }

    @Test
    public void validateWhenTypeIsEmpty(){
        boolean isValid = validator.isValid("", Mockito.mock(ConstraintValidatorContextImpl.class));
        assertEquals(false, isValid);
    }

    @Test
    public void validateWhenTypeIsBlank(){
        boolean isValid = validator.isValid("  ", Mockito.mock(ConstraintValidatorContextImpl.class));
        assertEquals(false, isValid);
    }

    @Test
    public void validateWhenTypeIsFULL(){
        boolean isValid = validator.isValid("full", Mockito.mock(ConstraintValidatorContextImpl.class));
        assertEquals(true, isValid);
    }

    @Test
    public void validateWhenTypeIsINVENTORY(){
        boolean isValid = validator.isValid("inventory", Mockito.mock(ConstraintValidatorContextImpl.class));
        assertEquals(true, isValid);
    }

    @Test
    public void validateWhenTypeIsPARTIAL(){
        boolean isValid = validator.isValid("partial", Mockito.mock(ConstraintValidatorContextImpl.class));
        assertEquals(true, isValid);
    }

}
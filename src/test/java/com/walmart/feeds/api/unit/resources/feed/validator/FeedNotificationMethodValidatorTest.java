package com.walmart.feeds.api.unit.resources.feed.validator;

import com.walmart.feeds.api.resources.feed.validator.FeedNotificationMethodValidator;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class FeedNotificationMethodValidatorTest {

    private FeedNotificationMethodValidator validator = new FeedNotificationMethodValidator();

    @Test
    public void validateWhenMethodIsInvalid(){
        boolean isValid = validator.isValid("teste123", Mockito.mock(ConstraintValidatorContextImpl.class));
        assertEquals(false, isValid);
    }

    @Test
    public void validateWhenMethodIsNull(){
        boolean isValid = validator.isValid(null, Mockito.mock(ConstraintValidatorContextImpl.class));
        assertEquals(false, isValid);
    }

    @Test
    public void validateWhenMethodIsEmpty(){
        boolean isValid = validator.isValid("", Mockito.mock(ConstraintValidatorContextImpl.class));
        assertEquals(false, isValid);
    }

    @Test
    public void validateWhenMethodIsBlank(){
        boolean isValid = validator.isValid("  ", Mockito.mock(ConstraintValidatorContextImpl.class));
        assertEquals(false, isValid);
    }

    @Test
    public void validateWhenMethodIsAPI(){
        boolean isValid = validator.isValid("api", Mockito.mock(ConstraintValidatorContextImpl.class));
        assertEquals(true, isValid);
    }

    @Test
    public void validateWhenMethodIsFILE(){
        boolean isValid = validator.isValid("file", Mockito.mock(ConstraintValidatorContextImpl.class));
        assertEquals(true, isValid);
    }

}
package com.walmart.feeds.api.unit.resources.feed.validator;

import com.walmart.feeds.api.resources.feed.validator.FeedNotificationFormatValidator;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class FeedNotificationFormatValidatorTest {

    private FeedNotificationFormatValidator validator = new FeedNotificationFormatValidator();

    @Test
    public void validateWhenFormatIsInvalid(){
        boolean isValid = validator.isValid("teste123", Mockito.mock(ConstraintValidatorContextImpl.class));
        assertEquals(false, isValid);
    }

    @Test
    public void validateWhenFormatIsNull(){
        boolean isValid = validator.isValid(null, Mockito.mock(ConstraintValidatorContextImpl.class));
        assertEquals(false, isValid);
    }

    @Test
    public void validateWhenFormatIsEmpty(){
        boolean isValid = validator.isValid("", Mockito.mock(ConstraintValidatorContextImpl.class));
        assertEquals(false, isValid);
    }

    @Test
    public void validateWhenFormatIsBlank(){
        boolean isValid = validator.isValid("  ", Mockito.mock(ConstraintValidatorContextImpl.class));
        assertEquals(false, isValid);
    }

    @Test
    public void validateWhenFormatIsJSON(){
        boolean isValid = validator.isValid("json", Mockito.mock(ConstraintValidatorContextImpl.class));
        assertEquals(true, isValid);
    }

    @Test
    public void validateWhenFormatIsXML(){
        boolean isValid = validator.isValid("xml", Mockito.mock(ConstraintValidatorContextImpl.class));
        assertEquals(true, isValid);
    }

    @Test
    public void validateWhenFormatIsTSV(){
        boolean isValid = validator.isValid("tsv", Mockito.mock(ConstraintValidatorContextImpl.class));
        assertEquals(true, isValid);
    }

}
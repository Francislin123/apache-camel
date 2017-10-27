package com.walmart.feeds.api.unit.resources.feed.validator;

import com.walmart.feeds.api.resources.feed.validator.CronValidator;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class CronPatternValidatorTest {


    @Test
    public void testValidCronPatter(){
        CronValidator cronValidator = new CronValidator();
        boolean valid = cronValidator.isValid("0 0 1 * * ?",  mock(ConstraintValidatorContextImpl.class));
        assertEquals(true, valid);
    }

    @Test
    public void testNullPattern(){
        CronValidator cronValidator = new CronValidator();
        boolean valid = cronValidator.isValid(null,  mock(ConstraintValidatorContextImpl.class));
        assertEquals(true, valid);
    }

    @Test
    public void invalidPattern(){
        CronValidator cronValidator = new CronValidator();
        boolean valid = cronValidator.isValid("invalid pattern",  mock(ConstraintValidatorContextImpl.class));
        assertEquals(false, valid);
    }
}

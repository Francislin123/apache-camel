package com.walmart.feeds.api.resources.feed.validator;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.walmart.feeds.api.resources.feed.request.FeedNotificationData;
import com.walmart.feeds.api.resources.feed.test.template.FeedTemplateLoader;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.validation.ConstraintValidatorContext;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class FeedNotificationDataURLValidatorTest {

    private FeedNotificationDataURLValidator validator = new FeedNotificationDataURLValidator();

    @BeforeClass
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.resources.feed.test.template");
    }

    @Test
    public void validateWhenIsTypeFile() throws Exception {
        boolean valid = validator.isValid(Fixture.from(FeedNotificationData.class).gimme("notification-file-valid"), mock(ConstraintValidatorContextImpl.class));
        assertEquals(true, valid);
    }

    @Test
    public void validateWhenIsTypeAPIAndUrlAreNull() {
        boolean valid = validator.isValid(Fixture.from(FeedNotificationData.class).gimme("notification-api-without-url"), mock(ConstraintValidatorContextImpl.class));
        assertEquals(false, valid);
    }

    @Test
    public void validateWhenNotificationObjIsNull() {
        boolean valid = validator.isValid(null, mock(ConstraintValidatorContextImpl.class));
        assertEquals(true, valid);
    }


}
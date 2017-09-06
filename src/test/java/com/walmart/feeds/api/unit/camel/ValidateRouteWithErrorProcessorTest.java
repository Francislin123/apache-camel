package com.walmart.feeds.api.unit.camel;

import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.walmart.feeds.api.camel.ValidateRouteWithErrorProcessor;
import com.walmart.feeds.api.core.exceptions.InvalidFileException;
import com.walmart.feeds.api.core.exceptions.SystemException;
import com.walmart.feeds.api.resources.common.response.FileError;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedList;
import java.util.List;

import static com.walmart.feeds.api.camel.PartnerTaxonomyRouteBuilder.ERROR_LIST;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ValidateRouteWithErrorProcessorTest {

    @InjectMocks
    private ValidateRouteWithErrorProcessor processor;

    private Exchange exchangeMock;

    @Before
    public void init() {
        exchangeMock = mock(Exchange.class);

        Message message = mock(Message.class);
        when(exchangeMock.getIn()).thenReturn(message);
    }

    @Test(expected = SystemException.class)
    public void testWhenErrorListIsNull() throws Exception {

        when(exchangeMock.getIn().getHeader(ERROR_LIST, List.class)).thenReturn(null);

        try {
            processor.process(exchangeMock);
        } catch (SystemException e) {
            assertNotNull("The message must be set in exception", e.getMessage());
            throw e;
        }

    }

    @Test(expected = InvalidFileException.class)
    public void testWhenErrorListIsNotEmpty() throws Exception {

        List errorList = new LinkedList();

        errorList.add(mock(FileError.class));

        when(exchangeMock.getIn().getHeader(ERROR_LIST, List.class)).thenReturn(errorList);

        try {
            processor.process(exchangeMock);
        } catch (InvalidFileException e) {
            assertNotNull("Exceptions must contain message", e.getMessage());
            assertNotNull("Exception must contain error list", e.getErrors());
            assertFalse("Exception must contain one element on list", e.getErrors().isEmpty());
            assertEquals(errorList, e.getErrors());
            throw e;
        }

    }

}
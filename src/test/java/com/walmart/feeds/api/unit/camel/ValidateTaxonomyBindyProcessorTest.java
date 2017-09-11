package com.walmart.feeds.api.unit.camel;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.walmart.feeds.api.camel.PartnerTaxonomyRouteBuilder;
import com.walmart.feeds.api.camel.TaxonomyMappingBindy;
import com.walmart.feeds.api.camel.ValidateTaxonomyBindyProcessor;
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

import static com.walmart.feeds.api.unit.camel.test.template.TaxonomyBindyTemplateLoader.*;
import static org.apache.camel.Exchange.SPLIT_INDEX;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ValidateTaxonomyBindyProcessorTest {

    @InjectMocks
    private ValidateTaxonomyBindyProcessor processor;

    private Exchange exchangeMock;

    @BeforeClass
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.camel.test.template");
    }

    @Before
    public void init() {
        exchangeMock = mock(Exchange.class);

        Message message = mock(Message.class);
        when(exchangeMock.getIn()).thenReturn(message);
    }

    @Test(expected = SystemException.class)
    public void testWhenErrorListIsNotSetInRoute() throws Exception {
        TaxonomyMappingBindy mappingBindy = Fixture.from(TaxonomyMappingBindy.class).gimme(VALID_TAXONOMY_BINDY);

        when(exchangeMock.getIn().getHeader(PartnerTaxonomyRouteBuilder.ERROR_LIST, List.class)).thenReturn(null);
        when(exchangeMock.getIn().getBody(TaxonomyMappingBindy.class)).thenReturn(mappingBindy);

        try {
            processor.process(exchangeMock);
        } catch (SystemException e) {
            assertNotNull("The message must be set in exception", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testProcessWhenTaxonomyDoNotContainWalmartTaxonomy() throws Exception {

        TaxonomyMappingBindy mappingBindy = Fixture.from(TaxonomyMappingBindy.class).gimme(BINDY_WITHOUT_WALMART_TAXONOMY);

        when(exchangeMock.getIn().getBody(TaxonomyMappingBindy.class)).thenReturn(mappingBindy);
        when(exchangeMock.getProperty(SPLIT_INDEX, Integer.class)).thenReturn(0);

        List errorsList = new LinkedList<FileError>();
        when(exchangeMock.getIn().getHeader(PartnerTaxonomyRouteBuilder.ERROR_LIST, List.class)).thenReturn(errorsList);

        processor.process(exchangeMock);

        FileError expectedFileError = FileError.builder()
                .line(1)
                .message("All columns must be informed")
                .build();

        assertEquals(1, errorsList.size());
        FileError errorElement = (FileError) errorsList.iterator().next();
        assertNotNull("Error element should not be null", errorElement);
        assertEquals(Integer.valueOf(1), errorElement.getLine());
        assertEquals("All columns must be informed", errorElement.getMessage());

    }

    @Test
    public void testProcessWhenTaxonomyDoNotContainPartnerTaxonomy() throws Exception {

        TaxonomyMappingBindy mappingBindy = Fixture.from(TaxonomyMappingBindy.class).gimme(BINDY_WITHOUT_PARTNER_TAXONOMY);

        when(exchangeMock.getIn().getBody(TaxonomyMappingBindy.class)).thenReturn(mappingBindy);
        when(exchangeMock.getProperty(SPLIT_INDEX, Integer.class)).thenReturn(0);

        List errorsList = new LinkedList<FileError>();
        when(exchangeMock.getIn().getHeader(PartnerTaxonomyRouteBuilder.ERROR_LIST, List.class)).thenReturn(errorsList);

        processor.process(exchangeMock);

        FileError expectedFileError = FileError.builder()
                .line(1)
                .message("All columns must be informed")
                .build();

        assertEquals(1, errorsList.size());
        FileError errorElement = (FileError) errorsList.iterator().next();
        assertNotNull("Error element should not be null", errorElement);
        assertEquals(Integer.valueOf(1), errorElement.getLine());
        assertEquals("All columns must be informed", errorElement.getMessage());

    }

    @Test
    public void testProcessWhenTaxonomyDoNotContainPartnerId() throws Exception {

        TaxonomyMappingBindy mappingBindy = Fixture.from(TaxonomyMappingBindy.class).gimme(BINDY_WITHOUT_ID);

        when(exchangeMock.getIn().getBody(TaxonomyMappingBindy.class)).thenReturn(mappingBindy);
        when(exchangeMock.getProperty(SPLIT_INDEX, Integer.class)).thenReturn(0);

        List errorsList = new LinkedList<FileError>();
        when(exchangeMock.getIn().getHeader(PartnerTaxonomyRouteBuilder.ERROR_LIST, List.class)).thenReturn(errorsList);

        processor.process(exchangeMock);

        FileError expectedFileError = FileError.builder()
                .line(1)
                .message("All columns must be informed")
                .build();

        assertEquals(1, errorsList.size());
        FileError errorElement = (FileError) errorsList.iterator().next();
        assertNotNull("Error element should not be null", errorElement);
        assertEquals(Integer.valueOf(1), errorElement.getLine());
        assertEquals("All columns must be informed", errorElement.getMessage());

    }

    @Test
    public void testProcessWhenTaxonomyIsValid() throws Exception {

        TaxonomyMappingBindy mappingBindy = Fixture.from(TaxonomyMappingBindy.class).gimme(VALID_TAXONOMY_BINDY);

        when(exchangeMock.getIn().getBody(TaxonomyMappingBindy.class)).thenReturn(mappingBindy);
        when(exchangeMock.getProperty(SPLIT_INDEX, Integer.class)).thenReturn(0);

        List errorsList = mock(LinkedList.class);
        when(exchangeMock.getIn().getHeader(PartnerTaxonomyRouteBuilder.ERROR_LIST, List.class)).thenReturn(errorsList);

        processor.process(exchangeMock);

        assertEquals(0, errorsList.size());
        verify(errorsList, never()).add(any(FileError.class));

    }

}
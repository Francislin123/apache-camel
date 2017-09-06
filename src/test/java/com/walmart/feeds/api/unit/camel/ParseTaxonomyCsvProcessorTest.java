package com.walmart.feeds.api.unit.camel;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.walmart.feeds.api.camel.ParseTaxonomyCsvProcessor;
import com.walmart.feeds.api.camel.TaxonomyMappingBindy;
import com.walmart.feeds.api.core.exceptions.SystemException;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import com.walmart.feeds.api.core.repository.taxonomy.model.TaxonomyMappingEntity;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedList;
import java.util.List;

import static com.walmart.feeds.api.camel.PartnerTaxonomyRouteBuilder.PERSISTED_PARTNER_TAXONOMY;
import static com.walmart.feeds.api.unit.camel.test.template.TaxonomyBindyTemplateLoader.VALID_TAXONOMY_BINDY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ParseTaxonomyCsvProcessorTest {

    @InjectMocks
    private ParseTaxonomyCsvProcessor processor;

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
    public void testWhenPartnerTaxonomyIsNotPresentInRoute() throws Exception {

        when(exchangeMock.getIn().getBody(List.class)).thenReturn(mock(List.class));
        when(exchangeMock.getIn().getHeader(PERSISTED_PARTNER_TAXONOMY, PartnerTaxonomyEntity.class)).thenReturn(null);

        try {
            processor.process(exchangeMock);
        } catch (SystemException e) {
            assertNotNull("Exception must contain a message", e.getMessage());
            throw e;
        }

    }

    @Test
    public void testWhenTaxonomiesParsedEmptyList() {

        List<TaxonomyMappingBindy> taxonomyMappingBindies = new LinkedList<>();

        when(exchangeMock.getIn().getBody(List.class)).thenReturn(taxonomyMappingBindies);
        when(exchangeMock.getIn().getHeader(PERSISTED_PARTNER_TAXONOMY, PartnerTaxonomyEntity.class)).thenReturn(null);

    }

    @Test
    public void testWhenTaxonomiesParsedHasOneElement() throws Exception {

        List<TaxonomyMappingBindy> taxonomyMappingBindies = Fixture.from(TaxonomyMappingBindy.class).gimme(1, VALID_TAXONOMY_BINDY);;
        PartnerTaxonomyEntity partnerTaxonomyEntity = mock(PartnerTaxonomyEntity.class);

        when(exchangeMock.getIn().getBody(List.class)).thenReturn(taxonomyMappingBindies);
        when(exchangeMock.getIn().getHeader(PERSISTED_PARTNER_TAXONOMY, PartnerTaxonomyEntity.class)).thenReturn(partnerTaxonomyEntity);
        when(exchangeMock.getOut()).thenReturn(mock(Message.class));

        processor.process(exchangeMock);

        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);

        verify(exchangeMock.getOut()).setBody(argumentCaptor.capture());
        assertEquals(taxonomyMappingBindies.size(), argumentCaptor.getValue().size());

        TaxonomyMappingEntity returnedTaxonomyMapping = (TaxonomyMappingEntity) argumentCaptor.getValue().get(0);

        assertNotNull("PartnerTaxonomy must be set", returnedTaxonomyMapping.getPartnerTaxonomy());
        assertEquals(partnerTaxonomyEntity, returnedTaxonomyMapping.getPartnerTaxonomy());

        assertNotNull("PartnerPath must be set", returnedTaxonomyMapping.getPartnerPath());
        assertEquals(taxonomyMappingBindies.get(0).getPartnerTaxonomy(), returnedTaxonomyMapping.getPartnerPath());

        assertNotNull("WalmartPath must be set", returnedTaxonomyMapping.getWalmartPath());
        assertEquals(taxonomyMappingBindies.get(0).getWalmartTaxonomy(), returnedTaxonomyMapping.getWalmartPath());

        assertNotNull("PartnerPathId must be set", returnedTaxonomyMapping.getPartnerPathId());
        assertEquals(taxonomyMappingBindies.get(0).getStructurePartnerId(), returnedTaxonomyMapping.getPartnerPathId());
    }

    @Test
    public void testWhenTaxonomiesParsedHasManyElements() throws Exception {

        List<TaxonomyMappingBindy> taxonomyMappingBindies = Fixture.from(TaxonomyMappingBindy.class).gimme(5, VALID_TAXONOMY_BINDY);;
        PartnerTaxonomyEntity partnerTaxonomyEntity = mock(PartnerTaxonomyEntity.class);

        when(exchangeMock.getIn().getBody(List.class)).thenReturn(taxonomyMappingBindies);
        when(exchangeMock.getIn().getHeader(PERSISTED_PARTNER_TAXONOMY, PartnerTaxonomyEntity.class)).thenReturn(partnerTaxonomyEntity);
        when(exchangeMock.getOut()).thenReturn(mock(Message.class));

        processor.process(exchangeMock);

        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);

        verify(exchangeMock.getOut()).setBody(argumentCaptor.capture());
        assertEquals(taxonomyMappingBindies.size(), argumentCaptor.getValue().size());

    }

}
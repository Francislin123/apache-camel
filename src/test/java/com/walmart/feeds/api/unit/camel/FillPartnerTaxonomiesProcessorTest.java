package com.walmart.feeds.api.unit.camel;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.walmart.feeds.api.camel.FillPartnerTaxonomiesProcessor;
import com.walmart.feeds.api.core.exceptions.SystemException;
import com.walmart.feeds.api.core.repository.taxonomy.model.ImportStatus;
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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.walmart.feeds.api.camel.PartnerTaxonomyRouteBuilder.PERSISTED_PARTNER_TAXONOMY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FillPartnerTaxonomiesProcessorTest {

    @InjectMocks
    private FillPartnerTaxonomiesProcessor processor;

    private Exchange exchangeMock;

    @BeforeClass
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.resources.taxonomy.test.template");
    }

    @Before
    public void init() {
        exchangeMock = mock(Exchange.class);

        Message message = mock(Message.class);
        when(exchangeMock.getIn()).thenReturn(message);
    }

    @Test(expected = SystemException.class)
    public void testWhenPartnerTaxonomyIsNotPresentInRoute() throws Exception {

        when(exchangeMock.getIn().getHeader(PERSISTED_PARTNER_TAXONOMY, PartnerTaxonomyEntity.class)).thenReturn(null);

        try {
            processor.process(exchangeMock);
        } catch (SystemException e) {
            assertNotNull("Exception must contain a message", e.getMessage());
            throw e;
        }

    }

    @Test
    public void testWhenPartnerTaxonomyDoNoHaveMappings() throws Exception {

        PartnerTaxonomyEntity partnerTaxonomyEntity = Fixture.from(PartnerTaxonomyEntity.class).gimme("cs-input-mapping-null");

        when(exchangeMock.getIn().getHeader(PERSISTED_PARTNER_TAXONOMY, PartnerTaxonomyEntity.class)).thenReturn(partnerTaxonomyEntity);
        when(exchangeMock.getOut()).thenReturn(mock(Message.class));

        ArgumentCaptor<PartnerTaxonomyEntity> argumentCaptor = ArgumentCaptor.forClass(PartnerTaxonomyEntity.class);

        processor.process(exchangeMock);

        verify(exchangeMock.getOut()).setBody(argumentCaptor.capture());

        assertNotNull("Id must be set", argumentCaptor.getValue().getId());
        assertNotNull("FileName must be set", argumentCaptor.getValue().getFileName());
        assertNotNull("Name must be set", argumentCaptor.getValue().getName());
        assertNotNull("Partner must be set", argumentCaptor.getValue().getPartner());
        assertNotNull("Slug must be set", argumentCaptor.getValue().getSlug());
        assertNotNull("Status must be set", argumentCaptor.getValue().getStatus());
        assertNull(argumentCaptor.getValue().getTaxonomyMappings());
        assertEquals(ImportStatus.PENDING, argumentCaptor.getValue().getStatus());
        assertEquals(partnerTaxonomyEntity.getTaxonomyMappings(), argumentCaptor.getValue().getTaxonomyMappings());

    }

    @Test
    public void testWhenPartnerTaxonomyDoHaveEmptyMappingList() throws Exception {

        PartnerTaxonomyEntity partnerTaxonomyEntity = Fixture.from(PartnerTaxonomyEntity.class).gimme("cs-input-mapping-empty");

        when(exchangeMock.getIn().getHeader(PERSISTED_PARTNER_TAXONOMY, PartnerTaxonomyEntity.class)).thenReturn(partnerTaxonomyEntity);
        when(exchangeMock.getIn().getBody(List.class)).thenReturn(Collections.emptyList());
        when(exchangeMock.getOut()).thenReturn(mock(Message.class));

        ArgumentCaptor<PartnerTaxonomyEntity> argumentCaptor = ArgumentCaptor.forClass(PartnerTaxonomyEntity.class);

        processor.process(exchangeMock);

        verify(exchangeMock.getOut()).setBody(argumentCaptor.capture());

        assertNotNull("Id must be set", argumentCaptor.getValue().getId());
        assertNotNull("FileName must be set", argumentCaptor.getValue().getFileName());
        assertNotNull("Name must be set", argumentCaptor.getValue().getName());
        assertNotNull("TaxonomyMappings must be set", argumentCaptor.getValue().getTaxonomyMappings());
        assertNotNull("Partner must be set", argumentCaptor.getValue().getPartner());
        assertNotNull("Slug must be set", argumentCaptor.getValue().getSlug());
        assertNotNull("Status must be set", argumentCaptor.getValue().getStatus());
        assertEquals(ImportStatus.PENDING, argumentCaptor.getValue().getStatus());
        assertEquals(partnerTaxonomyEntity.getTaxonomyMappings(), argumentCaptor.getValue().getTaxonomyMappings());

    }

    @Test
    public void testWhenPartnerTaxonomyInsertNewMapping() throws Exception {

        PartnerTaxonomyEntity partnerTaxonomyEntity = Fixture.from(PartnerTaxonomyEntity.class).gimme("cs-input-ok");

        List<TaxonomyMappingEntity> newMappingsList = new LinkedList<>(partnerTaxonomyEntity.getTaxonomyMappings());
        newMappingsList.add(mock(TaxonomyMappingEntity.class));

        when(exchangeMock.getIn().getHeader(PERSISTED_PARTNER_TAXONOMY, PartnerTaxonomyEntity.class)).thenReturn(partnerTaxonomyEntity);
        when(exchangeMock.getIn().getBody(List.class)).thenReturn(newMappingsList);
        when(exchangeMock.getOut()).thenReturn(mock(Message.class));

        ArgumentCaptor<PartnerTaxonomyEntity> argumentCaptor = ArgumentCaptor.forClass(PartnerTaxonomyEntity.class);

        processor.process(exchangeMock);

        verify(exchangeMock.getOut()).setBody(argumentCaptor.capture());

        assertNotNull("Id must be set", argumentCaptor.getValue().getId());
        assertNotNull("FileName must be set", argumentCaptor.getValue().getFileName());
        assertNotNull("Name must be set", argumentCaptor.getValue().getName());
        assertNotNull("TaxonomyMappings must be set", argumentCaptor.getValue().getTaxonomyMappings());
        assertNotNull("Partner must be set", argumentCaptor.getValue().getPartner());
        assertNotNull("Slug must be set", argumentCaptor.getValue().getSlug());
        assertNotNull("Status must be set", argumentCaptor.getValue().getStatus());
        assertEquals(ImportStatus.PENDING, argumentCaptor.getValue().getStatus());
        assertEquals(2, argumentCaptor.getValue().getTaxonomyMappings().size());

    }

    @Test
    public void testWhenPartnerTaxonomyRemoveMappingAndAddNewMapping() throws Exception {

        PartnerTaxonomyEntity partnerTaxonomyEntity = Fixture.from(PartnerTaxonomyEntity.class).gimme("cs-input-ok");

        List<TaxonomyMappingEntity> newMappingsList = new LinkedList<>();
        newMappingsList.add(mock(TaxonomyMappingEntity.class));

        when(exchangeMock.getIn().getHeader(PERSISTED_PARTNER_TAXONOMY, PartnerTaxonomyEntity.class)).thenReturn(partnerTaxonomyEntity);
        when(exchangeMock.getIn().getBody(List.class)).thenReturn(newMappingsList);
        when(exchangeMock.getOut()).thenReturn(mock(Message.class));

        ArgumentCaptor<PartnerTaxonomyEntity> argumentCaptor = ArgumentCaptor.forClass(PartnerTaxonomyEntity.class);

        processor.process(exchangeMock);

        verify(exchangeMock.getOut()).setBody(argumentCaptor.capture());

        assertNotNull("Id must be set", argumentCaptor.getValue().getId());
        assertNotNull("FileName must be set", argumentCaptor.getValue().getFileName());
        assertNotNull("Name must be set", argumentCaptor.getValue().getName());
        assertNotNull("TaxonomyMappings must be set", argumentCaptor.getValue().getTaxonomyMappings());
        assertNotNull("Partner must be set", argumentCaptor.getValue().getPartner());
        assertNotNull("Slug must be set", argumentCaptor.getValue().getSlug());
        assertNotNull("Status must be set", argumentCaptor.getValue().getStatus());
        assertEquals(ImportStatus.PENDING, argumentCaptor.getValue().getStatus());
        assertEquals(1, argumentCaptor.getValue().getTaxonomyMappings().size());

    }

    @Test
    public void testWhenPartnerTaxonomyRemoveAllMappings() throws Exception {

        PartnerTaxonomyEntity partnerTaxonomyEntity = Fixture.from(PartnerTaxonomyEntity.class).gimme("cs-input-ok");

        List<TaxonomyMappingEntity> newMappingsList = new LinkedList<>();

        when(exchangeMock.getIn().getHeader(PERSISTED_PARTNER_TAXONOMY, PartnerTaxonomyEntity.class)).thenReturn(partnerTaxonomyEntity);
        when(exchangeMock.getIn().getBody(List.class)).thenReturn(newMappingsList);
        when(exchangeMock.getOut()).thenReturn(mock(Message.class));

        ArgumentCaptor<PartnerTaxonomyEntity> argumentCaptor = ArgumentCaptor.forClass(PartnerTaxonomyEntity.class);

        processor.process(exchangeMock);

        verify(exchangeMock.getOut()).setBody(argumentCaptor.capture());

        assertNotNull("Id must be set", argumentCaptor.getValue().getId());
        assertNotNull("FileName must be set", argumentCaptor.getValue().getFileName());
        assertNotNull("Name must be set", argumentCaptor.getValue().getName());
        assertNotNull("TaxonomyMappings must be set", argumentCaptor.getValue().getTaxonomyMappings());
        assertNotNull("Partner must be set", argumentCaptor.getValue().getPartner());
        assertNotNull("Slug must be set", argumentCaptor.getValue().getSlug());
        assertNotNull("Status must be set", argumentCaptor.getValue().getStatus());
        assertEquals(ImportStatus.PENDING, argumentCaptor.getValue().getStatus());
        assertEquals(0, argumentCaptor.getValue().getTaxonomyMappings().size());

    }

}
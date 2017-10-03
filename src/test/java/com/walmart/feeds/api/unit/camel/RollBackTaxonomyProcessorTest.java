package com.walmart.feeds.api.unit.camel;

import com.walmart.feeds.api.camel.RollBackTaxonomyProcessor;
import com.walmart.feeds.api.core.exceptions.SystemException;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import com.walmart.feeds.api.core.service.taxonomy.PartnerTaxonomyService;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.walmart.feeds.api.camel.PartnerTaxonomyRouteBuilder.PERSISTED_PARTNER_TAXONOMY;
import static com.walmart.feeds.api.core.repository.taxonomy.model.ImportStatus.ERROR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RollBackTaxonomyProcessorTest {

    @InjectMocks
    private RollBackTaxonomyProcessor processor;

    @Mock
    private PartnerTaxonomyService partnerTaxonomyService;

    private Exchange exchangeMock;

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
            verify(partnerTaxonomyService, never()).saveWithHistory(any(PartnerTaxonomyEntity.class));
            throw e;
        }

    }

    @Test
    public void testWhenPartnerTaxonomyIsPresent() throws Exception {

        PartnerTaxonomyEntity mock = mock(PartnerTaxonomyEntity.class);

        ArgumentCaptor<PartnerTaxonomyEntity> argumentCaptor = ArgumentCaptor.forClass(PartnerTaxonomyEntity.class);

        when(exchangeMock.getIn().getHeader(PERSISTED_PARTNER_TAXONOMY, PartnerTaxonomyEntity.class)).thenReturn(mock);
        when(partnerTaxonomyService.saveWithHistory(any(PartnerTaxonomyEntity.class))).thenReturn(mock);

        processor.process(exchangeMock);

        verify(partnerTaxonomyService).saveWithHistory(argumentCaptor.capture());
        assertEquals(ERROR, argumentCaptor.getValue().getStatus());

    }

}
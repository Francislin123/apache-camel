package com.walmart.feeds.api.unit.camel;

import com.walmart.feeds.api.camel.TaxonomyMappingBindy;
import com.walmart.feeds.api.camel.ValidateDeletedTaxonomiesProcessor;
import com.walmart.feeds.api.core.exceptions.SystemException;
import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyBlacklistEntity;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import com.walmart.feeds.api.core.repository.taxonomy.model.TaxonomyMappingEntity;
import com.walmart.feeds.api.core.service.blacklist.taxonomy.TaxonomyBlacklistService;
import com.walmart.feeds.api.resources.common.response.FileError;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedList;
import java.util.List;

import static com.walmart.feeds.api.camel.PartnerTaxonomyRouteBuilder.ERROR_LIST;
import static com.walmart.feeds.api.camel.PartnerTaxonomyRouteBuilder.PERSISTED_PARTNER_TAXONOMY;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ValidateDeletedTaxonomiesProcessorTest {

    @InjectMocks
    private ValidateDeletedTaxonomiesProcessor processor;

    @Mock
    private TaxonomyBlacklistService taxonomyBlacklistService;

    private Exchange exchangeMock;

    @Before
    public void init() {
        exchangeMock = mock(Exchange.class);

        Message message = mock(Message.class);
        when(exchangeMock.getIn()).thenReturn(message);
    }

    @Test(expected = SystemException.class)
    public void testWhenPartnerTaxonomyIsNull() throws Exception {

        when(exchangeMock.getIn().getBody(List.class)).thenReturn(Mockito.mock(List.class));
        when(exchangeMock.getIn().getHeader(PERSISTED_PARTNER_TAXONOMY, PartnerTaxonomyEntity.class)).thenReturn(null);

        try {
            processor.process(exchangeMock);
        } catch (SystemException e) {
            assertNotNull("Exception must contain a message", e.getMessage());
            throw e;
        }

    }

    @Test
    public void testWhenTaxonomiesMappingIsNull() throws Exception {

        PartnerTaxonomyEntity taxonomyEntity = new PartnerTaxonomyEntity();

        when(exchangeMock.getIn().getBody(List.class)).thenReturn(Mockito.mock(List.class));
        when(exchangeMock.getIn().getHeader(PERSISTED_PARTNER_TAXONOMY, PartnerTaxonomyEntity.class)).thenReturn(taxonomyEntity);

        processor.process(exchangeMock);

        //do-nothing

    }

    @Test
    public void testWhenTaxonomiesMappingIsEmpty() throws Exception {

        PartnerTaxonomyEntity partnertTaxonomyEntity = PartnerTaxonomyEntity.builder()
                .taxonomyMappings(new LinkedList<>())
            .build();

        LinkedList errorList = mock(LinkedList.class);

        when(exchangeMock.getIn().getBody(List.class)).thenReturn(Mockito.mock(List.class));
        when(exchangeMock.getIn().getHeader(PERSISTED_PARTNER_TAXONOMY, PartnerTaxonomyEntity.class)).thenReturn(partnertTaxonomyEntity);
        when(exchangeMock.getIn().getHeader(ERROR_LIST, List.class)).thenReturn(errorList);

        processor.process(exchangeMock);

        verify(taxonomyBlacklistService, never()).findBlackList(anyString());
        verify(errorList, never()).add(any());

    }

    @Test
    public void testWhenTaxonomiesAssociatedToBlackListIsRemoved() throws Exception {

        LinkedList<TaxonomyMappingEntity> taxonomyMappings = new LinkedList<>();
        taxonomyMappings.add(TaxonomyMappingEntity.builder()
                .partnerPathId("id")
                .partnerPath("teste > teste1")
                .walmartPath("partner > partner teste")
                .build());

        PartnerTaxonomyEntity partnertTaxonomyEntity = PartnerTaxonomyEntity.builder()
                .taxonomyMappings(taxonomyMappings)
                .build();

        LinkedList errorList = mock(LinkedList.class);

        when(exchangeMock.getIn().getBody(List.class)).thenReturn(Mockito.mock(List.class));
        when(exchangeMock.getIn().getHeader(PERSISTED_PARTNER_TAXONOMY, PartnerTaxonomyEntity.class)).thenReturn(partnertTaxonomyEntity);
        when(exchangeMock.getIn().getHeader(ERROR_LIST, List.class)).thenReturn(errorList);
        when(taxonomyBlacklistService.findBlackList(anyString())).thenReturn(mock(TaxonomyBlacklistEntity.class));

        processor.process(exchangeMock);

        verify(taxonomyBlacklistService, times(2)).findBlackList(anyString());
        verify(errorList, times(2)).add(any());

    }

    @Test
    public void testWhenTaxonomiesNotAssociatedToBlackListIsRemoved() throws Exception {

        LinkedList<TaxonomyMappingEntity> taxonomyMappings = new LinkedList<>();
        taxonomyMappings.add(TaxonomyMappingEntity.builder()
                .partnerPathId("id")
                .partnerPath("teste > teste1")
                .walmartPath("partner > partner teste")
            .build());

        PartnerTaxonomyEntity partnertTaxonomyEntity = PartnerTaxonomyEntity.builder()
                .taxonomyMappings(taxonomyMappings)
                .build();

        List<FileError> errorList = mock(LinkedList.class);

        when(exchangeMock.getIn().getBody(List.class)).thenReturn(Mockito.mock(List.class));
        when(exchangeMock.getIn().getHeader(PERSISTED_PARTNER_TAXONOMY, PartnerTaxonomyEntity.class)).thenReturn(partnertTaxonomyEntity);
        when(exchangeMock.getIn().getHeader(ERROR_LIST, List.class)).thenReturn(errorList);
        when(taxonomyBlacklistService.findBlackList(anyString())).thenReturn(null);

        processor.process(exchangeMock);

        verify(taxonomyBlacklistService, times(2)).findBlackList(anyString());
        verify(errorList, never()).add(any());

    }

}
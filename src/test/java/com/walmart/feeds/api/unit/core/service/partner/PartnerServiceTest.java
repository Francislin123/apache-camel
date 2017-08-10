package com.walmart.feeds.api.unit.core.service.partner;

import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.exceptions.InconsistentEntityException;
import com.walmart.feeds.api.core.repository.partner.PartnerHistoryRepository;
import com.walmart.feeds.api.core.repository.partner.PartnerRepository;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.core.repository.partner.model.PartnerHistory;
import com.walmart.feeds.api.core.service.partner.PartnerService;
import com.walmart.feeds.api.core.service.partner.PartnerServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PartnerServiceTest {

    private Logger logger = LoggerFactory.getLogger(PartnerServiceTest.class);

    @Mock
    private PartnerRepository repository;

    @Mock
    private PartnerHistoryRepository historyRepository;

    @InjectMocks
    private PartnerService service = new PartnerServiceImpl();

    @Before
    public void setUp() throws NoSuchMethodException {

    }

    //--------------------------------------------------------------------------------------------------------------//
    @Test
    public void testUpdatePartner() {
        try {
            when(repository.findBySlug(anyString())).thenReturn(Optional.of(new PartnerEntity()));
            when(repository.saveAndFlush(any(PartnerEntity.class))).thenReturn(createPartner());

            PartnerEntity request = PartnerEntity.builder().name("Teste 123").slug("teste").build();

            this.service.updatePartner(request);

            verify(repository).findBySlug(anyString());
            verify(repository).saveAndFlush(Mockito.any(PartnerEntity.class));
            verify(historyRepository).save(any(PartnerHistory.class));
        } catch (EntityNotFoundException e) {
            fail("Exception should not have been fired!");
        }
    }

    @Test(expected = InconsistentEntityException.class)
    public void testCreatePartnerWIthEmptyPartnershipList() {

        PartnerEntity partner = PartnerEntity.builder()
                .name("PartnerEntity")
                .slug("partner")
                .description("New partner")
                .partnerships("").build();

        service.savePartner(partner);
    }

    @Test
    public void testSearchPartners() {
        when(repository.searchPartners(anyString())).thenReturn(Arrays.asList(createPartner()));
        List<PartnerEntity> partners = service.searchPartners("bus");
        assertFalse(partners.isEmpty());
    }

    @Test
    public void testSearchPartnersEmptyResult() {
        when(repository.searchPartners(anyString())).thenReturn(new ArrayList<>());
        List<PartnerEntity> partners = service.searchPartners("xyz");
        assertTrue(partners.isEmpty());
    }

    private PartnerEntity createPartner() {
        PartnerEntity partner = PartnerEntity.builder()
                .name("PartnerEntity")
                .slug("partner")
                .description("New partner")
                .partnerships("teste123").build();

        return partner;
    }

}

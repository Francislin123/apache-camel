package com.walmart.feeds.api.unit.core.service.partner;

import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.InconsistentEntityException;
import com.walmart.feeds.api.core.exceptions.UserException;
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
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.QueryTimeoutException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PartnerServiceTest {

    public static final String PARTNER_NAME = "PartnerEntity";
    public static final String PARTNER_SLUG = "partnerentity";
    public static final String DIVERGENT_SLUG = "partner";

    private Logger logger = LoggerFactory.getLogger(PartnerServiceTest.class);

    @Mock
    private PartnerRepository partnerRepository;

    @Mock
    private PartnerHistoryRepository historyRepository;

    @InjectMocks
    private PartnerService partnerService = new PartnerServiceImpl();

    @Before
    public void setUp() throws NoSuchMethodException {

    }

    //--------------------------------------------------------------------------------------------------------------//

    @Test
    public void testUpdatePartner() {

        when(partnerRepository.findBySlug(PARTNER_SLUG)).thenReturn(Optional.of(createPartner()));
        when(partnerRepository.saveAndFlush(any(PartnerEntity.class))).thenReturn(createPartner());

        PartnerEntity request = PartnerEntity.builder().name(PARTNER_NAME).slug(PARTNER_SLUG).build();
        this.partnerService.update(request);

        verify(partnerRepository).findBySlug(PARTNER_SLUG);
        verify(partnerRepository).saveAndFlush(Mockito.any(PartnerEntity.class));
        verify(historyRepository).save(any(PartnerHistory.class));

    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void testUpdatePartnerWhenOccursConflict() {

        PartnerEntity partner = PartnerEntity.builder().name(PARTNER_NAME).slug(DIVERGENT_SLUG).build();

        when(partnerRepository.findBySlug(anyString())).thenReturn(Optional.of(new PartnerEntity()));

        this.partnerService.update(partner);

    }

    @Test(expected = InconsistentEntityException.class)
    public void testCreatePartnerWIthEmptyPartnershipList() {

        PartnerEntity partner = PartnerEntity.builder()
                .name(PARTNER_NAME)
                .slug(PARTNER_SLUG)
                .description("New partner")
                .partnerships("").build();

        partnerService.save(partner);
    }

    @Test
    public void testSearchPartners() {

        String queryParam = "bus";
        when(partnerRepository.searchPartners(queryParam)).thenReturn(Arrays.asList(createPartner()));

        List<PartnerEntity> partners = partnerService.search(queryParam);
        assertFalse(partners.isEmpty());

    }

    @Test
    public void testSearchPartnersEmptyResult() {

        String queryParam = "xyz";
        when(partnerRepository.searchPartners(queryParam)).thenReturn(new ArrayList<>());

        List<PartnerEntity> partners = partnerService.search(queryParam);
        assertTrue(partners.isEmpty());

    }

    @Test(expected = UserException.class)
    public void testSearchPartnersWhenQueryParamIsNull() {
        List<PartnerEntity> partners = partnerService.search(null);
    }

    @Test(expected = DataAccessException.class)
    public void testSearchPartnersWhenDatabaseIsDown() {

        when(partnerRepository.searchPartners(anyString())).thenThrow(QueryTimeoutException.class);
        List<PartnerEntity> partners = partnerService.search("buscape");

    }

    private PartnerEntity createPartner() {
        PartnerEntity partner = PartnerEntity.builder()
                .name(PARTNER_NAME)
                .slug(PARTNER_SLUG)
                .description("New partner")
                .partnerships("teste123").build();

        return partner;
    }

    private PartnerEntity createPartnerUpdateName() {
        PartnerEntity partner = PartnerEntity.builder()
                .name(PARTNER_NAME)
                .slug(DIVERGENT_SLUG)
                .description("New partner")
                .partnerships("teste123").build();

        return partner;
    }

}

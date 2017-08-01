package com.walmart.feeds.api.unit.core.service.partner;

import com.walmart.feeds.api.core.exceptions.NotFoundException;
import com.walmart.feeds.api.core.repository.partner.PartnerHistoryRepository;
import com.walmart.feeds.api.core.repository.partner.PartnerRepository;
import com.walmart.feeds.api.core.repository.partner.model.Partner;
import com.walmart.feeds.api.core.repository.partner.model.PartnerHistory;
import com.walmart.feeds.api.core.service.partner.PartnerService;
import com.walmart.feeds.api.core.service.partner.PartnerServiceImpl;
import com.walmart.feeds.api.core.service.partner.model.PartnerTO;
import com.walmart.feeds.api.resources.partner.request.PartnerRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;

import javax.validation.ConstraintViolationException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasItem;
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

    private Method buildPartnerMethod;

    private Method buildPartnerTOMethod;

    @Before
    public void setUp() throws NoSuchMethodException {
        buildPartnerMethod = PartnerServiceImpl.class
                .getDeclaredMethod("buildPartner", PartnerTO.class);
        buildPartnerMethod.setAccessible(true);

        buildPartnerTOMethod = PartnerServiceImpl.class.getDeclaredMethod("buildPartnerTO",
                Partner.class);
        buildPartnerTOMethod.setAccessible(true);

    }

    @Test
    public void testBuildPartnerFromPartnerRequest()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        PartnerTO partnerRequest = createPartnerTO();
        partnerRequest.setPartnerships(Arrays.asList("SEM", "COMPARADOR"));

        Partner partner = (Partner) buildPartnerMethod.invoke(service, partnerRequest);

        assertEquals(partnerRequest.getName(), partner.getName());
        assertEquals(partnerRequest.getReference(), partner.getReference());
        assertEquals(partnerRequest.getDescription(), partner.getDescription());
        assertEquals(partner.getPartnerships(), "SEM;COMPARADOR");
        logger.info("Mapped partnerships: {}", partner.getPartnerships());
    }

    @Test
    public void testBuildPartnerTOFromPartnerWithPartnerships()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Partner partner = new Partner();
        partner.setActive(true);
        partner.setName("Buscape");
        partner.setReference("buscape");
        partner.setPartnerships("comparadores;afiliados");

        PartnerTO partnerResponse = (PartnerTO) buildPartnerTOMethod.invoke(service, partner);

        assertEquals(partnerResponse.getName(), partnerResponse.getName());
        assertEquals(partnerResponse.getReference(), partnerResponse.getReference());
        assertEquals(partnerResponse.getDescription(), partnerResponse.getDescription());
        assertThat(partnerResponse.getPartnerships(), hasItem("comparadores"));
        logger.info("Partnerships: {} ", partnerResponse.getPartnerships());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildPartnerFromNullPartnerRequest()
            throws NoSuchMethodException, IllegalAccessException {

        try {
            PartnerRequest partnerRequest = null;
            Partner partner = (Partner) buildPartnerMethod.invoke(service, partnerRequest);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof IllegalArgumentException)
                throw (IllegalArgumentException) e.getCause();
        }

    }

    @Test
    public void testUpdatePartner() {
        try {
            when(repository.findByReference(anyString()))
                    .thenReturn(Optional.of(new Partner()));

            PartnerTO request = createPartnerTO();
            this.service.updatePartner(request);

            verify(repository).findByReference(anyString());
            verify(repository).save(Mockito.any(Partner.class));
            verify(historyRepository).save(any(PartnerHistory.class));
        } catch (NotFoundException e) {
            fail("Exception should not have been fired!");
        }
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateInexistentPartnerShouldThrowNotFoundException() throws NotFoundException {
        when(repository.findByReference(anyString())).thenReturn(Optional.empty());
        PartnerTO partnerTO = createPartnerTO();
        this.service.updatePartner(partnerTO);
        verify(repository).findByReference(anyString());
        verify(repository, times(0)).save(Mockito.any(Partner.class));
        verify(historyRepository, times(0)).save(Mockito.any(PartnerHistory.class));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testCreatePartnerWIthEmptyPartnershipList() {
        when(repository.save(Mockito.any(Partner.class))).thenThrow(DataIntegrityViolationException.class);
        PartnerTO partnerTO = createPartnerTO();
        service.savePartner(partnerTO);
        verify(repository).save(Mockito.any(Partner.class));
    }

    @Test
    public void testSearchPartners() {
        when(repository.searchPartners(anyString())).thenReturn(Arrays.asList(createPartner()));
        List<PartnerTO> partners = service.searchPartners("bus");
        assertFalse(partners.isEmpty());
    }

    @Test
    public void testSearchPartnersEmptyResult() {
        when(repository.searchPartners(anyString())).thenReturn(new ArrayList<>());
        List<PartnerTO> partners = service.searchPartners("xyz");
        assertTrue(partners.isEmpty());
    }

    private Partner createPartner() {
        Partner partner = new Partner();
        partner.setName("Partner");
        partner.setReference("partner");
        partner.setDescription("New partner");
        return partner;
    }

    private PartnerTO createPartnerTO() {
        PartnerTO to = new PartnerTO();
        to.setName("Partner");
        to.setReference("partner");
        to.setDescription("New partner");
        to.setPartnerships(Arrays.asList("comparadores"));
        return to;
    }

}

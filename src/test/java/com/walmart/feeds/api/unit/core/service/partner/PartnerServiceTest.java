package com.walmart.feeds.api.unit.core.service.partner;

import com.walmart.feeds.api.core.repository.partner.PartnerRepository;
import com.walmart.feeds.api.core.repository.partner.PartnershipRepository;
import com.walmart.feeds.api.core.repository.partner.model.Partner;
import com.walmart.feeds.api.core.repository.partner.model.Partnership;
import com.walmart.feeds.api.core.service.partner.PartnerService;
import com.walmart.feeds.api.core.service.partner.PartnerServiceImpl;
import com.walmart.feeds.api.resources.partner.request.PartnerRequest;
import com.walmart.feeds.api.resources.partner.response.PartnerResponse;
import javassist.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PartnerServiceTest {

    private Logger logger = LoggerFactory.getLogger(PartnerServiceTest.class);

    @Mock
    private PartnerRepository repository;

    @InjectMocks
    private PartnerService service = new PartnerServiceImpl();

    private Method buildPartnerMethod;

    @Before
    public void setUp() throws NoSuchMethodException {
        buildPartnerMethod = PartnerServiceImpl.class
                .getDeclaredMethod("buildPartner", PartnerRequest.class);
        buildPartnerMethod.setAccessible(true);

    }

    @Test
    public void testBuildPartnerFromPartnerRequest()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        PartnerRequest partnerRequest = createPartnerRequest();
        partnerRequest.setPartnership(Arrays.asList("SEM", "COMPARADOR"));

        Partner partner = (Partner) buildPartnerMethod.invoke(service, partnerRequest);

        assertEquals(partnerRequest.getName(), partner.getName());
        assertEquals(partnerRequest.getReference(), partner.getReference());
        assertEquals(partnerRequest.getDescription(), partner.getDescription());
        assertEquals(partner.getPartnerships(), "SEM;COMPARADOR");
        logger.info("Mapped partnerships: {}", partner.getPartnerships());
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

    @Test(expected = IllegalArgumentException.class)
    public void testUpdatePartnerFromNullPartnerRequestShouldReturnFalse() throws NotFoundException {
        this.service.updatePartner(null);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateInexistentPartnerShouldThrowNotFoundException() throws NotFoundException {
        when(repository.findByReference(anyString())).thenReturn(Optional.empty());
        PartnerRequest request = createPartnerRequest();
        this.service.updatePartner(request);
        verify(repository, times(1)).findByReference(anyString());
        verify(repository, times(0)).save(Mockito.any(Partner.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatePartnerWIthEmptyPartnershipList() {
        service.savePartner(createPartnerRequest());
    }

    @Test
    public void testUpdatePartnerShouldReturnTrue() {
        try {
            when(repository.findByReference(anyString()))
                    .thenReturn(Optional.of(new Partner()));
            PartnerRequest request = new PartnerRequest();

            this.service.updatePartner(request);

            verify(repository, times(1)).findByReference(anyString());
            verify(repository, times(1)).save(Mockito.any(Partner.class));
        } catch (NotFoundException e) {
            fail("Exception should not have been fired!");
        }
    }

    @Test
    public void testSearchPartners() {
        when(repository.searchPartners(anyString())).thenReturn(Arrays.asList(createPartner()));
        List<PartnerResponse> partners = service.searchPartners("bus");
        assertFalse(partners.isEmpty());
    }

    @Test
    public void testSearchPartnersEmptyResult() {
        when(repository.searchPartners(anyString())).thenReturn(new ArrayList<Partner>());
        List<PartnerResponse> partners = service.searchPartners("xyz");
        assertTrue(partners.isEmpty());
    }

    private PartnerRequest createPartnerRequest() {
        PartnerRequest partnerRequest = new PartnerRequest();
        partnerRequest.setName("Partner");
        partnerRequest.setReference("partner");
        partnerRequest.setDescription("New partner");
        return partnerRequest;
    }

    private PartnerResponse createPartnerResponse() {
        PartnerResponse partnerResponse = new PartnerResponse();
        partnerResponse.setName("Partner");
        partnerResponse.setReference("partner");
        partnerResponse.setDescription("New partner");
        return partnerResponse;
    }

    private Partner createPartner() {
        Partner partner = new Partner();
        partner.setName("Partner");
        partner.setReference("partner");
        partner.setDescription("New partner");
        return partner;
    }

}

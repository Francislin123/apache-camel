package com.walmart.feeds.api.unit.core.service.partner;

import com.walmart.feeds.api.core.repository.partner.PartnerRepository;
import com.walmart.feeds.api.core.repository.partner.PartnershipRepository;
import com.walmart.feeds.api.core.repository.partner.model.Partner;
import com.walmart.feeds.api.core.repository.partner.model.Partnership;
import com.walmart.feeds.api.core.service.partner.PartnerService;
import com.walmart.feeds.api.core.service.partner.PartnerServiceImpl;
import com.walmart.feeds.api.resources.partner.request.PartnerRequest;
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

    @Mock
    private PartnershipRepository psRepository;

    @InjectMocks
    private PartnerService service = new PartnerServiceImpl();

    private Method buildPartnerMethod;

    @Before
    public void setUp() throws NoSuchMethodException {
        when(psRepository.findOne(anyString()))
                .then((invocation) -> {
                    Partnership partnership = new Partnership(invocation.getArgumentAt(0, String.class));
                    logger.info("Partnership: {}", partnership.getName());
                    return partnership;
                });

        buildPartnerMethod = PartnerServiceImpl.class
                .getDeclaredMethod("buildPartner", PartnerRequest.class);
        buildPartnerMethod.setAccessible(true);

    }

    @Test
    public void testBuildPartnerFromPartnerRequestWithPartnerships()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        PartnerRequest partnerRequest = createPartnerRequest();
        partnerRequest.setPartnership(Arrays.asList("SEM", "COMPARADOR"));

        Partner partner = (Partner) buildPartnerMethod.invoke(service, partnerRequest);

        assertEquals(partnerRequest.getName(), partner.getName());
        assertEquals(partnerRequest.getReference(), partner.getReference());
        assertEquals(partnerRequest.getDescription(), partner.getDescription());
        assertThat(partner.getPartnership(), hasItem(new Partnership("SEM")));
        assertThat(partner.getPartnership(), hasItem(new Partnership("COMPARADOR")));
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
    public void testBuildPartnerFromPartnerRequestWithoutPartnerships()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        PartnerRequest partnerRequest = createPartnerRequest();
        partnerRequest.setPartnership(new ArrayList<>());

        Partner partner = (Partner) buildPartnerMethod.invoke(service, partnerRequest);

        assertEquals(partnerRequest.getName(), partner.getName());
        assertEquals(partnerRequest.getReference(), partner.getReference());
        assertEquals(partnerRequest.getDescription(), partner.getDescription());
        assertThat("Partnership list should be empty",
                partner.getPartnership(), empty());
        logger.info("Partnership size: {}", partner.getPartnership().size());
    }

    @Test
    public void testNonNullPartnershipsFromPartnerRequest()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        PartnerRequest partnerRequest = createPartnerRequest();
        partnerRequest.setPartnership(null);

        Partner partner = (Partner) buildPartnerMethod.invoke(service, partnerRequest);

        assertEquals(partnerRequest.getName(), partner.getName());
        assertEquals(partnerRequest.getReference(), partner.getReference());
        assertEquals(partnerRequest.getDescription(), partner.getDescription());
        assertThat("Partnership list should be empty",
                partner.getPartnership(), empty());
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
        when(psRepository.findByReference(anyString())).thenReturn(null);
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

    private PartnerRequest createPartnerRequest() {
        PartnerRequest partnerRequest = new PartnerRequest();
        partnerRequest.setName("Partner");
        partnerRequest.setReference("partner");
        partnerRequest.setDescription("New partner");
        return partnerRequest;
    }

}

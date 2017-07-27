package com.walmart.feeds.api.unit.resources.partner;

import com.walmart.feeds.api.core.service.partner.PartnerService;
import com.walmart.feeds.api.resources.partner.PartnerController;
import com.walmart.feeds.api.resources.partner.request.PartnerRequest;
import com.walmart.feeds.api.resources.partner.response.PartnerResponse;
import javassist.NotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.NoResultException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PartnerControllerTest {

    public static final String URI_PARTNERS = "/v1/partners";
    private Logger logger = LoggerFactory.getLogger(PartnerControllerTest.class);

    @InjectMocks //@Autowired
    private PartnerController controller = new PartnerController();

    @Mock
    private PartnerService partnerService;

    @Test
    public void testCreateNewPartner() throws Exception {
        PartnerRequest request = new PartnerRequest();
        controller.createPartner(request);
        Mockito.verify(partnerService, Mockito.times(1)).savePartner(request);
    }

    @Test
    public void testCreatedNewPartnerWithInexistenPartnership(){
        PartnerRequest request = new PartnerRequest();
        Mockito.doThrow(IllegalArgumentException.class).when(partnerService).savePartner(request);
        ResponseEntity response = controller.createPartner(request);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testCreatedNewWithConflict(){
        PartnerRequest request = new PartnerRequest();
        Mockito.doThrow(DataIntegrityViolationException.class).when(partnerService).savePartner(request);
        ResponseEntity response = controller.createPartner(request);
        Assert.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testFetchAllPartners() {
        when(partnerService.findAllPartners()).thenReturn(Arrays.asList(new PartnerResponse()));

        ResponseEntity<List<PartnerResponse>> response = controller.fetchAllPartners();

        verify(partnerService).findAllPartners();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    public void testFetchAllPartnersWithDatabaseDown() {
        // TODO map the correct exception
        doThrow(Exception.class).when(partnerService).findAllPartners();

        ResponseEntity response = controller.fetchAllPartners();

        verify(partnerService).findAllPartners();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testFetchActivePartners(){
        Mockito.when(partnerService.findActivePartners()).thenReturn(Arrays.asList(new PartnerResponse()));
        ResponseEntity<List<PartnerResponse>> partnersActives = controller.fetchPartnerActives();
        Assert.assertNotNull(partnersActives);
        Assert.assertTrue(!partnersActives.getBody().isEmpty());
    }

    @Test
    public void testFetchActivePartnersWithDatabaseDown(){
        Mockito.doThrow(Exception.class).when(partnerService).findActivePartners();
        ResponseEntity<List<PartnerResponse>> partnersActives = controller.fetchPartnerActives();
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, partnersActives.getStatusCode());
    }

    @Test
    public void testUpdatePartner() throws Exception {
        ResponseEntity<?> response = controller.updatePartner("anyReference", getPartnerRequest());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdatePartnerNotfoundWhenInexistentPartner() throws Exception {
        doThrow(NotFoundException.class).when(partnerService).updatePartner(any(PartnerRequest.class));
        ResponseEntity<?> response = controller.updatePartner("anyReference", getPartnerRequest());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdatePartnerWithDatabaseDown() throws Exception {
        doThrow(Exception.class).when(partnerService).updatePartner(any(PartnerRequest.class));
        ResponseEntity<?> response = controller.updatePartner("anyReference", getPartnerRequest());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testChangePartnerStatus() {
        ResponseEntity response = controller.changePartnerStatus("buscape", "0");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testChangePartnerStatusWhenDatabaseDown() {
        doThrow(Exception.class).when(partnerService).setPartnerStatus(anyString(), anyBoolean());
        ResponseEntity response = controller.changePartnerStatus("buscape", "0");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    private PartnerRequest getPartnerRequest() {
        PartnerRequest partnerRequest = new PartnerRequest();
        return partnerRequest;
    }

    private PartnerResponse getPartnerResponse() {
        PartnerResponse partnerResponse = new PartnerResponse();
        return partnerResponse;
    }
}

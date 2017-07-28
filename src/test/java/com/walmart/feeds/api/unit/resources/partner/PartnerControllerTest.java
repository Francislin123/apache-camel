package com.walmart.feeds.api.unit.resources.partner;

import com.walmart.feeds.api.core.service.partner.PartnerService;
import com.walmart.feeds.api.core.service.partner.model.PartnerTO;
import com.walmart.feeds.api.resources.partner.PartnerController;
import com.walmart.feeds.api.resources.partner.request.PartnerRequest;
import com.walmart.feeds.api.resources.partner.response.PartnerResponseList;
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
        Mockito.verify(partnerService, Mockito.times(1)).savePartner(Mockito.any(PartnerTO.class));
    }

    @Test
    public void testCreatedNewPartnerWithInexistenPartnership(){
        PartnerRequest request = new PartnerRequest();
        Mockito.doThrow(IllegalArgumentException.class).when(partnerService).savePartner(Mockito.any(PartnerTO.class));
        ResponseEntity response = controller.createPartner(request);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testCreatedNewWithConflict(){
        PartnerRequest request = new PartnerRequest();
        Mockito.doThrow(DataIntegrityViolationException.class).when(partnerService).savePartner(Mockito.any(PartnerTO.class));
        ResponseEntity response = controller.createPartner(request);
        Assert.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testFetchAllPartners() {
        when(partnerService.findAllPartners()).thenReturn(Arrays.asList(new PartnerTO()));

        ResponseEntity response = controller.fetchAllPartners();

        verify(partnerService).findAllPartners();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof PartnerResponseList);
        PartnerResponseList listResponse = (PartnerResponseList) response.getBody();
        assertFalse(listResponse.getPartners().isEmpty());
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
        Mockito.when(partnerService.findActivePartners()).thenReturn(Arrays.asList(new PartnerTO()));
        ResponseEntity<List<PartnerResponse>> response = controller.fetchPartnerActives();
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getBody() instanceof PartnerResponseList);
        PartnerResponseList listResponse = (PartnerResponseList) response.getBody();
        Assert.assertFalse(listResponse.getPartners().isEmpty());
    }

    @Test
    public void testFetchActivePartnersWithDatabaseDown(){
        Mockito.doThrow(Exception.class).when(partnerService).findActivePartners();
        ResponseEntity<List<PartnerResponse>> partnersActives = controller.fetchPartnerActives();
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, partnersActives.getStatusCode());
    }

    @Test
    public void testUpdatePartner() throws Exception {
        ResponseEntity<?> response = controller.updatePartner("anyReference", createPartnerRequest());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdatePartnerNotfoundWhenInexistentPartner() throws Exception {
        doThrow(NotFoundException.class).when(partnerService).updatePartner(any(PartnerTO.class));
        ResponseEntity<?> response = controller.updatePartner("anyReference", createPartnerRequest());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdatePartnerWithDatabaseDown() throws Exception {
        doThrow(Exception.class).when(partnerService).updatePartner(any(PartnerTO.class));
        ResponseEntity<?> response = controller.updatePartner("anyReference", createPartnerRequest());

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

    @Test
    public void testSearchPartners() {
        when(partnerService.searchPartners(anyString())).thenReturn(Arrays.asList(new PartnerTO()));
        ResponseEntity response = controller.searchPartners("busc");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testSearchPartnersEmptyResult() {
        when(partnerService.searchPartners(anyString())).thenReturn(Arrays.asList());
        ResponseEntity response = controller.searchPartners("xyz");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private PartnerRequest createPartnerRequest() {
        PartnerRequest partnerRequest = new PartnerRequest();
        return partnerRequest;
    }

}

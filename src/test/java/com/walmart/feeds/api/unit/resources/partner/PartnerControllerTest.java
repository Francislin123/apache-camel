package com.walmart.feeds.api.unit.resources.partner;

import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.core.service.partner.PartnerService;
import com.walmart.feeds.api.resources.feed.CollectionResponse;
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

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        PartnerRequest request = PartnerRequest.builder().name("teste123 lala").partnership(Arrays.asList("teste123")).build();
        controller.createPartner(request, null);
        verify(partnerService, Mockito.times(1)).savePartner(any(PartnerEntity.class));
    }

    @Test
    public void testCreatedNewPartnerWithInexistenPartnership(){
        PartnerRequest request = PartnerRequest.builder().name("teste123 lala").partnership(Arrays.asList("teste123")).build();
        doThrow(IllegalArgumentException.class).when(partnerService).savePartner(any(PartnerEntity.class));
        ResponseEntity response = controller.createPartner(request, null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testCreatedNewWithConflict(){
        PartnerRequest request = PartnerRequest.builder().name("teste123 lala").partnership(Arrays.asList("teste123")).build();
        doThrow(DataIntegrityViolationException.class).when(partnerService).savePartner(any(PartnerEntity.class));
        ResponseEntity response = controller.createPartner(request, null);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testFetchAllPartners() {
        when(partnerService.findAllPartners()).thenReturn(Arrays.asList(PartnerEntity.builder().partnerships("teste").build()));

        ResponseEntity response = controller.fetchAllPartners();

        verify(partnerService).findAllPartners();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof CollectionResponse);
        CollectionResponse listResponse = (CollectionResponse) response.getBody();
        assertFalse(listResponse.getResult().isEmpty());
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
        when(partnerService.findActivePartners()).thenReturn(Arrays.asList(PartnerEntity.builder().partnerships("teste").build()));
        ResponseEntity<CollectionResponse<PartnerResponse>> response = controller.fetchPartnerActives();
        Assert.assertNotNull(response);
        assertTrue(response.getBody() instanceof CollectionResponse);
        CollectionResponse listResponse = (CollectionResponse) response.getBody();
        assertFalse(listResponse.getResult().isEmpty());
    }

    @Test
    public void testFetchActivePartnersWithDatabaseDown(){
        doThrow(Exception.class).when(partnerService).findActivePartners();
        ResponseEntity<CollectionResponse<PartnerResponse>> partnersActives = controller.fetchPartnerActives();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, partnersActives.getStatusCode());
    }

    @Test
    public void testUpdatePartner() throws Exception {
        ResponseEntity<?> response = controller.updatePartner("anyReference", createPartnerRequest());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdatePartnerNotfoundWhenInexistentPartner() throws Exception {
        doThrow(NotFoundException.class).when(partnerService).updatePartner(any(PartnerEntity.class));
        ResponseEntity<?> response = controller.updatePartner("anyReference", createPartnerRequest());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdatePartnerWithDatabaseDown() throws Exception {
        doThrow(Exception.class).when(partnerService).updatePartner(any(PartnerEntity.class));
        ResponseEntity<?> response = controller.updatePartner("anyReference", createPartnerRequest());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testChangePartnerStatus() {
        ResponseEntity response = controller.changePartnerStatus("buscape", false);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testChangePartnerStatusWhenDatabaseDown() {
        doThrow(Exception.class).when(partnerService).setPartnerStatus(anyString(), anyBoolean());
        ResponseEntity response = controller.changePartnerStatus("buscape", false);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testSearchPartners() {
        when(partnerService.searchPartners(anyString())).thenReturn(Arrays.asList(PartnerEntity.builder().partnerships("teste").build()));
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
        return PartnerRequest.builder().name("teste123 lala").partnership(Arrays.asList("teste123")).build();
    }

}

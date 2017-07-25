package com.walmart.feeds.api.unit.resources.partner;

import com.walmart.feeds.api.core.repository.partner.PartnerRepository;
import com.walmart.feeds.api.core.repository.partner.model.Partner;
import com.walmart.feeds.api.core.service.partner.PartnerService;
import com.walmart.feeds.api.resources.partner.PartnerController;
import com.walmart.feeds.api.resources.partner.request.PartnerRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)//(SpringRunner.class)
//@WebMvcTest
public class PartnerControllerTest {

    public static final String URI_PARTNERS = "/v1/partners";
    private Logger logger = LoggerFactory.getLogger(PartnerControllerTest.class);

    @InjectMocks //@Autowired
    private PartnerController controller;

    @Mock
    private PartnerService partnerService;

    @Test
    public void testCreateNewPartner() throws Exception {
        PartnerRequest request = new PartnerRequest();
        controller.createPartner(request);
        Mockito.verify(partnerService, Mockito.times(1)).savePartner(request);
    }

    @Test
    public void testCreatedNewWithConflict(){
        PartnerRequest request = new PartnerRequest();
        Mockito.doThrow(DataIntegrityViolationException.class).when(partnerService).savePartner(request);
        ResponseEntity response = controller.createPartner(request);
        Assert.assertEquals(409, response.getStatusCodeValue());
    }

    @Test
    public void testGetPartnersActives(){
        Mockito.when(partnerService.findPartnerActives()).thenReturn(Arrays.asList(new Partner()));
        ResponseEntity<List<Partner>> partnersActives = controller.findPartnerActives();
        Assert.assertNotNull(partnersActives);
        Assert.assertTrue(!partnersActives.getBody().isEmpty());
    }

    @Test
    public void testGetPartnersActivesWithDatabaseDown(){
        Mockito.doThrow(Exception.class).when(partnerService).findPartnerActives();
        ResponseEntity<List<Partner>> partnersActives = controller.findPartnerActives();
        Assert.assertEquals(404, partnersActives.getStatusCodeValue());
    }

    @Test
    public void testUpdatePartner() throws Exception {
        when(partnerService.updatePartner(any(PartnerRequest.class))).thenReturn(true);
        ResponseEntity<?> response = controller.updatePartner("anyReference", getPartnerRequest());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdatePartnerWithDatabaseDown() throws Exception {
        when(partnerService.updatePartner(any(PartnerRequest.class))).thenThrow(new QueryTimeoutException("erro"));
        ResponseEntity<?> response = controller.updatePartner("anyReference", getPartnerRequest());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUpdatePartnerNotfoundWhenInexistentPartner() throws Exception {
        when(partnerService.updatePartner(any(PartnerRequest.class))).thenReturn(false);
        ResponseEntity<?> response = controller.updatePartner("anyReference", getPartnerRequest());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private PartnerRequest getPartnerRequest() {
        PartnerRequest partnerRequest = new PartnerRequest();
        return partnerRequest;
    }

}

package com.walmart.feeds.api.unit.resources.partner;

import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walmart.feeds.api.core.exceptions.NotFoundException;
import com.walmart.feeds.api.core.service.partner.PartnerService;
import com.walmart.feeds.api.resources.feed.CollectionResponse;
import com.walmart.feeds.api.resources.feed.response.ErrorResponse;
import com.walmart.feeds.api.resources.infrastructure.FeedsAdminAPIExceptionHandler;
import com.walmart.feeds.api.resources.partner.PartnerController;
import com.walmart.feeds.api.resources.partner.request.PartnerRequest;
import com.walmart.feeds.api.resources.partner.request.PartnerUpdateRequest;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import com.walmart.feeds.api.resources.partner.response.PartnerResponse;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PartnerControllerTest {

    public static final String URI_PARTNERS = "/v1/partners";

    private Logger logger = LoggerFactory.getLogger(PartnerControllerTest.class);

    private MockMvc mockMvc;

    private ObjectMapper mapper;

    @InjectMocks
    private PartnerController partnerController = new PartnerController();

    @Mock
    private PartnerService partnerService;

    @BeforeClass
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.resources.partner.test.template");
    }

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(partnerController)
                .setControllerAdvice(new FeedsAdminAPIExceptionHandler())
                .build();
        mapper = new ObjectMapper();
    }

    @Test
    public void testCreateNewPartner() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI_PARTNERS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(PartnerRequest.class)
                        .gimme("valid_partner_request"))))
            .andExpect(MockMvcResultMatchers.status().isCreated());

        Mockito.verify(partnerService).savePartner(Mockito.any(PartnerEntity.class));
    }

    @Test
    public void testCreatedNewPartnerWithInexistenPartnership() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI_PARTNERS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(PartnerRequest.class)
                    .gimme("invalid_partner_request_no_partnerships"))))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("400")));

        verify(partnerService, times(0)).savePartner(Mockito.any(PartnerEntity.class));
    }

    @Test
    public void testCreatedNewWithConflict() throws Exception {
        Mockito.doThrow(DataIntegrityViolationException.class)
                .when(partnerService).savePartner(Mockito.any(PartnerEntity.class));

        mockMvc.perform(MockMvcRequestBuilders.post(URI_PARTNERS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(PartnerRequest.class)
                    .gimme("valid_partner_request"))))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void testFetchAllPartners() throws Exception {
        when(partnerService.findPartnersByStatus(null)).thenReturn(Fixture.from(PartnerEntity.class).gimme(2, "partner_entity"));

        mockMvc.perform(MockMvcRequestBuilders.get(URI_PARTNERS).contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> logger.info("Result: {}", result.getResponse().getContentAsString())                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.result[0].slug", Matchers.is("buscape")))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(partnerService).findPartnersByStatus(null);
    }

    @Test
    public void testFetchAllPartnersWithDatabaseDown() throws Exception {
        when(partnerService.findPartnersByStatus(anyBoolean())).thenThrow(Exception.class);

        mockMvc.perform(MockMvcRequestBuilders.get(URI_PARTNERS)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(MockMvcResultMatchers.status().isInternalServerError());

        verify(partnerService).findPartnersByStatus(anyBoolean());
    }

    @Test
    public void testFetchActivePartners() throws Exception {
        when(partnerService.findPartnersByStatus(true)).thenReturn(Fixture.from(PartnerEntity.class).gimme(2, "partner_entity"));

        mockMvc.perform(MockMvcRequestBuilders.get(URI_PARTNERS)
                .param("active", "true")
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andDo(result -> {
                        logger.info("Result: {}", result.getResponse().getContentAsString());
                    })
                .andExpect(MockMvcResultMatchers.jsonPath("$.result[0].active", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(partnerService).findPartnersByStatus(true);
    }

    @Test
    public void testFetchActivePartnersWithDatabaseDown() throws Exception {
        when(partnerService.findPartnersByStatus(anyBoolean())).thenThrow(Exception.class);

        mockMvc.perform(MockMvcRequestBuilders.get(URI_PARTNERS).param("active", "true")
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());

        verify(partnerService).findPartnersByStatus(anyBoolean());
    }

    @Test
    public void testUpdatePartner() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URI_PARTNERS + "/reference")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(jsonRequest(Fixture.from(PartnerUpdateRequest.class).gimme("partner_update_request"))))
            .andExpect(MockMvcResultMatchers.status().isOk());

        verify(partnerService).updatePartner(Mockito.any(PartnerEntity.class));
    }

    @Test
    public void testUpdatePartnerNotfoundWhenInexistentPartner() throws Exception {
        doThrow(NotFoundException.class).when(partnerService).updatePartner(Mockito.any(PartnerEntity.class));

        mockMvc.perform(MockMvcRequestBuilders.put(URI_PARTNERS + "/reference")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(jsonRequest(Fixture.from(PartnerUpdateRequest.class).gimme("partner_update_request"))))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(partnerService).updatePartner(Mockito.any(PartnerEntity.class));
    }

    @Test
    public void testUpdatePartnerWithDatabaseDown() throws Exception {
        doThrow(Exception.class).when(partnerService).updatePartner(any(PartnerEntity.class));

        mockMvc.perform(MockMvcRequestBuilders.put(URI_PARTNERS + "/reference")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(jsonRequest(Fixture.from(PartnerUpdateRequest.class).gimme("partner_update_request"))))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());

        verify(partnerService).updatePartner(Mockito.any(PartnerEntity.class));
    }

    @Test
    public void testChangePartnerStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(URI_PARTNERS + "/reference?active=false")
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(partnerService).changePartnerStatus("reference", false);
    }

    @Test
    public void testChangePartnerStatusWhenDatabaseDown() throws Exception {
        doThrow(Exception.class).when(partnerService).changePartnerStatus("reference", false);

        mockMvc.perform(MockMvcRequestBuilders.patch(URI_PARTNERS + "/reference?active=false")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(PartnerUpdateRequest.class).gimme("partner_update_request"))))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());

        verify(partnerService).changePartnerStatus("reference", false);
    }

    @Test
    public void testSearchPartners() throws Exception {
        when(partnerService.searchPartners("busc")).thenReturn(Fixture.from(PartnerEntity.class).gimme(2, "partner_entity"));

        mockMvc.perform(MockMvcRequestBuilders.get(URI_PARTNERS + "/search?q=busc")
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result[0].slug", Matchers.is("buscape")));

        verify(partnerService).searchPartners("busc");
    }

    @Test
    public void testSearchPartnersEmptyResult() throws Exception {
        when(partnerService.searchPartners("test")).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get(URI_PARTNERS + "/search?q=test")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(partnerService).searchPartners("test");
    }

    @Test
    public void testSearchPartnersEmptyQuery() throws Exception {
        when(partnerService.searchPartners("")).thenReturn(Fixture.from(PartnerEntity.class).gimme(2, "partner_entity"));

        mockMvc.perform(MockMvcRequestBuilders.get(URI_PARTNERS + "/search?q=")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(partnerService).searchPartners("");
    }

    public String jsonRequest(Object request) throws JsonProcessingException {
        return mapper.writeValueAsString(request);
    }

}

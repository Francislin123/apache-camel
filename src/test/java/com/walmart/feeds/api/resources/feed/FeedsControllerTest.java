package com.walmart.feeds.api.resources.feed;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.walmart.feeds.api.core.exceptions.NotFoundException;
import com.walmart.feeds.api.core.service.feed.FeedServiceImpl;
import com.walmart.feeds.api.core.service.feed.model.FeedTO;
import com.walmart.feeds.api.resources.feed.request.FeedRequest;
import com.walmart.feeds.api.resources.feed.response.FeedResponse;
import com.walmart.feeds.api.resources.infrastructure.FeedsAdminAPIExceptionHandler;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by vn0y942 on 24/07/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class FeedsControllerTest {

    @InjectMocks
    private FeedsController feedsController;

    @Mock
    private FeedServiceImpl feedService;

    private static MockMvc mockMvc;

    @BeforeClass
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.resources.feed.test.template");
    }

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(feedsController)
                .setControllerAdvice(new FeedsAdminAPIExceptionHandler())
                .build();
    }

    @Test
    public void testCreateFeedSuccessfully() throws Exception {

        doNothing().when(feedService).createFeed(any(FeedTO.class));

        mockMvc.perform(
                post(FeedsController.V1_FEEDS, "partnerReferenceTest").contentType(MediaType.APPLICATION_JSON).content(asJsonString(Fixture.from(FeedRequest.class).gimme("feed-full-api-valid")))
        ).andExpect(status().isCreated())
                .andExpect(header().string("location", "http://localhost/v1/partners/partnerReferenceTest/feeds/feed_test"));

        verify(feedService, times(1)).createFeed(any(FeedTO.class));


    }

    @Test
    public void testCreateFeedWhenPartnerNotExists() throws Exception {
        doThrow(NotFoundException.class).when(feedService).createFeed(any(FeedTO.class));

        mockMvc.perform(
                post(FeedsController.V1_FEEDS, "partnerReferenceTest").contentType(MediaType.APPLICATION_JSON).content(asJsonString(Fixture.from(FeedRequest.class).gimme("feed-full-api-valid")))
        ).andExpect(status().isNotFound());

        verify(feedService, times(1)).createFeed(any(FeedTO.class));

    }

    @Test
    public void testCreateFeedWhenReferenceAlreadyExists() throws Exception {

        doThrow(DuplicateKeyException.class).when(feedService).createFeed(any(FeedTO.class));

        mockMvc.perform(
                post(FeedsController.V1_FEEDS, "partnerReferenceTest").contentType(MediaType.APPLICATION_JSON).content(asJsonString(Fixture.from(FeedRequest.class).gimme("feed-full-api-valid")))
        ).andExpect(status().isConflict());

        verify(feedService, times(1)).createFeed(any(FeedTO.class));
    }

    @Test
    public void testCreateFeedWhenRequestIsIvalid() throws Exception {

        doNothing().when(feedService).createFeed(any(FeedTO.class));

        mockMvc.perform(
                post(FeedsController.V1_FEEDS, "partnerReferenceTest").contentType(MediaType.APPLICATION_JSON).content(asJsonString(Fixture.from(FeedRequest.class).gimme("feed-full-without-name")))
        ).andExpect(status().isBadRequest());

        verify(feedService, times(0)).createFeed(any(FeedTO.class));
    }

    @Test
    public void testFeedsByPartnerAndReturnAList() throws Exception {
        FeedTO feedTO = new FeedTO();
        feedTO.setPartnerReference("AAA333");
        Mockito.when(feedService.fetchByPartner(feedTO)).thenReturn(this.mockListFeed());
        ResponseEntity<List<FeedResponse>> response = feedsController.fetchAll("AAA333");
        Mockito.verify(feedService).fetchByPartner(feedTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    public void testFeedsByPartnerAndDealWithUnknownPartner() throws NotFoundException {
        FeedTO feedTO = new FeedTO();
        feedTO.setPartnerReference("AAA333");
        Mockito.when(feedService.fetchByPartner(feedTO)).thenThrow(new NotFoundException(""));
        ResponseEntity response = feedsController.fetchAll("AAA333");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testFeedsActivesByPartnerAndReturnAList() throws Exception {
        FeedTO feedTO = new FeedTO();
        feedTO.setPartnerReference("AAA333");
        Mockito.when(feedService.fetchByActiveAndByPartner(feedTO)).thenReturn(this.mockListFeed());
        ResponseEntity<List<FeedResponse>> response = feedsController.fetchActives("AAA333");
        Mockito.verify(feedService).fetchByActiveAndByPartner(feedTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testFeedsActivesByPartnerAndDealWithUnknownPartner() throws NotFoundException {
        FeedTO feedTO = new FeedTO();
        feedTO.setPartnerReference("AAA333");
        Mockito.when(feedService.fetchByActiveAndByPartner(feedTO)).thenThrow(new NotFoundException(""));
        ResponseEntity response = feedsController.fetchActives("AAA333");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    public void testUpdateFeed() throws Exception {
        doNothing().when(feedService).updateFeed(Fixture.from(FeedTO.class).gimme("feed-to-full-api-valid"));
        mockMvc.perform(patch(FeedsController.V1_FEEDS, "partnerReferenceTest").contentType(MediaType.APPLICATION_JSON).content(asJsonString(Fixture.from(FeedRequest.class).gimme("feed-full-api-valid")))
                ).andExpect(status().isOk());
    }
    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateFeedAndDealWithDuplicatedReference() throws Exception {
        doThrow(DataIntegrityViolationException.class).when(feedService).updateFeed(Fixture.from(FeedTO.class).gimme("feed-to-full-api-valid"));
        mockMvc.perform(patch(FeedsController.V1_FEEDS, "partnerReferenceTest").contentType(MediaType.APPLICATION_JSON).content(asJsonString(Fixture.from(FeedRequest.class).gimme("feed-full-api-valid")))
        ).andExpect(status().isConflict());
    }
    private List<FeedTO> mockListFeed(){
        List<FeedTO> list = new ArrayList<>();
        FeedTO feedTO = new FeedTO();
        feedTO.setPartnerReference("AAA333");
        feedTO.setActive(true);
        list.add(feedTO);
        return list;
    }

    private String asJsonString(FeedRequest feedRequest) {
        try {
            return new Jackson2ObjectMapperBuilder().build().writeValueAsString(feedRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing request object to json");
        }
    }

}
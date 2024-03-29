package com.walmart.feeds.api.unit.resources.feed;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.walmart.feeds.api.client.tagadmin.TagAdmimCollectionClient;
import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.exceptions.InvalidFeedException;
import com.walmart.feeds.api.core.repository.feed.model.FeedEntity;
import com.walmart.feeds.api.core.service.blacklist.taxonomy.exceptions.TaxonomyBlacklistNotFoundException;
import com.walmart.feeds.api.core.service.blacklist.taxonomy.exceptions.TaxonomyBlacklistPartnerException;
import com.walmart.feeds.api.core.service.blacklist.taxonomy.exceptions.TermsBlacklistNotFoundException;
import com.walmart.feeds.api.core.service.feed.FeedServiceImpl;
import com.walmart.feeds.api.resources.feed.FeedsController;
import com.walmart.feeds.api.resources.feed.request.FeedRequest;
import com.walmart.feeds.api.resources.infrastructure.FeedsAdminAPIExceptionHandler;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Mock
    private TagAdmimCollectionClient tagAdmimCollectionClient;

    private static MockMvc mockMvc;

    @BeforeClass
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.resources.feed.test.template");
    }

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(feedsController)
                .setControllerAdvice(new FeedsAdminAPIExceptionHandler())
                .build();
    }

    @Test
    public void testCreateFeedSuccessfully() throws Exception {

        when(feedService.createFeed(any(FeedEntity.class))).thenReturn(FeedEntity.builder().slug("feed-test").build());

        mockMvc.perform(
                post(FeedsController.V1_FEEDS, "partnerReferenceTest").contentType(MediaType.APPLICATION_JSON).content(asJsonString(Fixture.from(FeedRequest.class).gimme("feed-full-api-valid")))
        ).andExpect(status().isCreated()).andExpect(header().string("location", "http://localhost/v1/partners/partnerReferenceTest/feeds/feed-test"));

        verify(feedService, times(1)).createFeed(any(FeedEntity.class));


    }

    @Test
    public void testCreateFeedWhenPartnerNotExists() throws Exception {
        doThrow(EntityNotFoundException.class).when(feedService).createFeed(any(FeedEntity.class));

        mockMvc.perform(
                post(FeedsController.V1_FEEDS, "partnerReferenceTest")
                        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(Fixture.from(FeedRequest.class).gimme("feed-full-api-valid")))
        ).andExpect(status().isNotFound());

        verify(feedService, times(1)).createFeed(any(FeedEntity.class));

    }

    @Test
    public void testCreateFeedWhenTaxonomyBlacklistNotExists() throws Exception {
        doThrow(TaxonomyBlacklistNotFoundException.class).when(feedService).createFeed(any(FeedEntity.class));

        mockMvc.perform(
                post(FeedsController.V1_FEEDS, "partnerReferenceTest")
                        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(Fixture.from(FeedRequest.class).gimme("feed-full-api-valid")))
        ).andExpect(status().isBadRequest());

        verify(feedService).createFeed(any(FeedEntity.class));

    }

    @Test
    public void testCreateFeedWhenTermsBlacklistNotExists() throws Exception {
        doThrow(TermsBlacklistNotFoundException.class).when(feedService).createFeed(any(FeedEntity.class));

        mockMvc.perform(
                post(FeedsController.V1_FEEDS, "partnerReferenceTest")
                        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(Fixture.from(FeedRequest.class).gimme("feed-full-api-valid")))
        ).andExpect(status().isBadRequest());

        verify(feedService).createFeed(any(FeedEntity.class));

    }

    @Test
    public void testCreateFeedWhenTaxonomyBlacklistContainsInvalidPartnerTaxonomy() throws Exception {
        doThrow(TaxonomyBlacklistPartnerException.class).when(feedService).createFeed(any(FeedEntity.class));

        mockMvc.perform(
                post(FeedsController.V1_FEEDS, "partnerReferenceTest")
                        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(Fixture.from(FeedRequest.class).gimme("feed-full-api-valid")))
        ).andExpect(status().isBadRequest());

        verify(feedService, times(1)).createFeed(any(FeedEntity.class));

    }

    @Test
    public void testCreateFeedWhenReferenceAlreadyExists() throws Exception {

        doThrow(EntityAlreadyExistsException.class).when(feedService).createFeed(any(FeedEntity.class));

        mockMvc.perform(
                post(FeedsController.V1_FEEDS, "partnerReferenceTest").contentType(MediaType.APPLICATION_JSON).content(asJsonString(Fixture.from(FeedRequest.class).gimme("feed-full-api-valid")))
        ).andExpect(status().isConflict());

        verify(feedService, times(1)).createFeed(any(FeedEntity.class));
    }

    @Test
    public void testCreateFeedWithInvalidUtmList() throws Exception {

        mockMvc.perform(
                post(FeedsController.V1_FEEDS, "partnerReferenceTest").contentType(MediaType.APPLICATION_JSON).content(asJsonString(Fixture.from(FeedRequest.class).gimme("feed-full-with-invalid-utm-list")))
        ).andExpect(status().isBadRequest());

        verify(feedService, times(0)).createFeed(any(FeedEntity.class));
    }

    @Test
    public void testCreateFeedWithAnEmptyName() throws Exception {

        mockMvc.perform(
                post(FeedsController.V1_FEEDS, "partnerReferenceTest").contentType(MediaType.APPLICATION_JSON).content(asJsonString(Fixture.from(FeedRequest.class).gimme("feed-full-with-empty-name")))
        ).andExpect(status().isBadRequest());

        verify(feedService, times(0)).createFeed(any(FeedEntity.class));
    }

    @Test
    public void testCreateFeedWhenRequestIsIvalid() throws Exception {

        mockMvc.perform(
                post(FeedsController.V1_FEEDS, "partnerReferenceTest").contentType(MediaType.APPLICATION_JSON).content(asJsonString(Fixture.from(FeedRequest.class).gimme("feed-full-without-name")))
        ).andExpect(status().isBadRequest());

        verify(feedService, times(0)).createFeed(any(FeedEntity.class));
    }

    @Test
    public void testUpdateFeed() throws Exception {
        doNothing().when(feedService).updateFeed(any(FeedEntity.class));
        mockMvc.perform(put(FeedsController.V1_FEEDS + "/teste123", "partnerReferenceTest").contentType(MediaType.APPLICATION_JSON).content(asJsonString(Fixture.from(FeedRequest.class).gimme("feed-full-api-valid")))
        ).andExpect(status().isOk());
    }

    @Test
    public void testUpdateFeedAndDealWithDuplicatedReference() throws Exception {
        doThrow(EntityAlreadyExistsException.class).when(feedService).updateFeed(any(FeedEntity.class));
        mockMvc.perform(put(FeedsController.V1_FEEDS + "/teste123", "partnerReferenceTest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(Fixture.from(FeedRequest.class).gimme("feed-full-api-valid"))))
                .andExpect(status().isConflict());
    }

    @Test
    public void testFeedValid() throws Exception {
        doNothing().when(feedService).validateFeed("partnerSlug", "feedSlug");
        mockMvc.perform(get(FeedsController.V1_FEEDS + "/feedSlug/validation", "partnerSlug")).andExpect(status().isOk());
    }

    @Test
    public void testFeedValidationInactivePartner() throws Exception {
        doThrow(InvalidFeedException.class).when(feedService).validateFeed("inactivePartner", "feedSlug");
        mockMvc.perform(get(FeedsController.V1_FEEDS + "/feedSlug/validation", "inactivePartner")).andExpect(status().isBadRequest());
    }

    private String asJsonString(FeedRequest feedRequest) {
        try {
            return new Jackson2ObjectMapperBuilder().build().writeValueAsString(feedRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing request object to json");
        }
    }

}
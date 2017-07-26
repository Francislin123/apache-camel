package com.walmart.feeds.api.resources.feed;

import com.walmart.feeds.api.core.exceptions.NotFoundException;
import com.walmart.feeds.api.core.service.feed.FeedService;
import com.walmart.feeds.api.core.service.feed.model.FeedTO;
import com.walmart.feeds.api.resources.feed.response.FeedResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by vn0y942 on 24/07/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class FeedsControllerTest {

    public FeedService feedService = Mockito.mock(FeedService.class);

    @InjectMocks
    private FeedsController feedsController;

    private MockMvc mockMvc;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(feedsController).build();
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

    @Test
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
        Mockito.when(feedService.fetchByActiveAndByPartnerId(feedTO)).thenReturn(this.mockListFeed());
        ResponseEntity<List<FeedResponse>> response = feedsController.fetchActives("AAA333");
        Mockito.verify(feedService).fetchByActiveAndByPartnerId(feedTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testFeedsActivesByPartnerAndDealWithUnknownPartner() throws NotFoundException {
        FeedTO feedTO = new FeedTO();
        feedTO.setPartnerReference("AAA333");
        Mockito.when(feedService.fetchByActiveAndByPartnerId(feedTO)).thenThrow(new NotFoundException(""));
        ResponseEntity response = feedsController.fetchActives("AAA333");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private List<FeedTO> mockListFeed(){
        List<FeedTO> list = new ArrayList<>();
        FeedTO feedTO = new FeedTO();
        feedTO.setPartnerReference("AAA333");
        feedTO.setActive(true);
        list.add(feedTO);
        return list;
    }

}
package com.walmart.feeds.api.unit.core.persistence;

import com.walmart.feeds.api.core.persistence.elasticsearch.ElasticSearchService;
import com.walmart.feeds.api.core.persistence.elasticsearch.ElasticSearchServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ElasticServiceTest {

    @InjectMocks
    private ElasticSearchService elasticSearchService = new ElasticSearchServiceImpl();

    @Mock
    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void validateWalmartTx(){

        String taxonomy = "Computadores > Hardware";

        when(elasticsearchTemplate.count(any(NativeSearchQuery.class))).thenReturn(1L);

        Boolean bool = this.elasticSearchService.validateWalmartTaxonomy(taxonomy);

        assertEquals(true, bool);

    }

    @Test
    public void invalidateWalmartTx(){
        String taxonomy = "Computadores > Hardware";

        when(elasticsearchTemplate.count(any(NativeSearchQuery.class))).thenReturn(0L);

        Boolean bool = this.elasticSearchService.validateWalmartTaxonomy(taxonomy);

        assertEquals(false, bool);
    }

    @Test
    public void invalidTaxonomyString(){
            String taxonomy = "";

            Boolean bool = this.elasticSearchService.validateWalmartTaxonomy(taxonomy);

            assertEquals(false, bool);
    }
}

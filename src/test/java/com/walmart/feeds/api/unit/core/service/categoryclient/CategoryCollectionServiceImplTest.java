package com.walmart.feeds.api.unit.core.service.categoryclient;

import com.walmart.feeds.api.client.categoryCollection.CategoryClient;
import com.walmart.feeds.api.client.tagadmin.TagAdmimCollectionClient;
import com.walmart.feeds.api.core.service.feed.CategoryCollectionServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by Francislin Dos Reis on 04/12/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class CategoryCollectionServiceImplTest {

    @InjectMocks
    private CategoryCollectionServiceImpl categoryCollectionService;

    @Mock
    private TagAdmimCollectionClient tagAdmimCollectionClient;

    @Test
    public void testValidStatusTaxonomyBlackList() {

        CategoryClient categoryClient = CategoryClient.builder().status(true).build();
        when(tagAdmimCollectionClient.findByCategoryName("Informática > Notebooks")).thenReturn(categoryClient);

        categoryCollectionService.validateTaxonomy("Informática > Notebooks");

        assertTrue(categoryClient.isStatus() == true);
    }

    @Test
    public void testInvalidStatusTaxonomyBlackList() {
        CategoryClient categoryClient = CategoryClient.builder().status(false).build();
        when(tagAdmimCollectionClient.findByCategoryName("Informática > Notebooks")).thenReturn(categoryClient);

        categoryCollectionService.validateTaxonomy("Informática > Notebooks");

        assertTrue(categoryClient.isStatus() == false);
    }

}

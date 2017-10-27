package com.walmart.feeds.api.unit.core.service.tagadmin;

import com.walmart.feeds.api.client.tagadmin.TagAdmimCollectionClient;
import com.walmart.feeds.api.client.tagadmin.TagAdminCollection;
import com.walmart.feeds.api.core.exceptions.UserException;
import com.walmart.feeds.api.core.service.feed.ProductCollectionServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

/**
 * Created by vn0gshm on 31/08/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductCollectionServiceImplTest {

    @InjectMocks
    private ProductCollectionServiceImpl productCollectionServiceImpl;

    @Mock
    private TagAdmimCollectionClient tagAdmimCollectionClient;

    @Test(expected = UserException.class)
    public void testFetchCollectionWhenTagAdminCollectionIsInactive() {

        TagAdminCollection tagAdminCollection = TagAdminCollection.builder().status("INACTIVE").build();
        when(tagAdmimCollectionClient.findById(7380L)).thenReturn(tagAdminCollection);

        productCollectionServiceImpl.validateCollectionExists(7380L);
    }

    @Test
    public void testFetchCollectionWhenTagAdminCollectionIsActive() {

        TagAdminCollection adminCollection = TagAdminCollection.builder().status("ACTIVE").build();
        when(tagAdmimCollectionClient.findById(1234L)).thenReturn(adminCollection);

        productCollectionServiceImpl.validateCollectionExists(1234L);
        verify(tagAdmimCollectionClient, times(1)).findById(1234L);
    }

    @Test(expected = UserException.class)
    public void testTagAdminCollectionNull() {

        when(tagAdmimCollectionClient.findById(1111L)).thenReturn(null);

        productCollectionServiceImpl.validateCollectionExists(1111L);
        verify(tagAdmimCollectionClient, times(1)).findById(null);
    }
}

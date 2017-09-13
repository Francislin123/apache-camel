package com.walmart.feeds.api.core.service.feed;

import com.walmart.feeds.api.client.tagadmin.TagAdmimCollectionClient;
import com.walmart.feeds.api.client.tagadmin.TagAdminCollection;
import com.walmart.feeds.api.core.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by vn0gshm on 31/08/17.
 */
@Service
public class ProductCollectionServiceImpl implements ProductCollectionService {

    @Autowired
    private TagAdmimCollectionClient tagAdminCollectionClient;

    @Override
    public void validateCollectionExists(Long collectionId) {

        TagAdminCollection tagAdminCollection = tagAdminCollectionClient.findById(collectionId);

        if (tagAdminCollection == null || !"ACTIVE".equals(tagAdminCollection.getStatus())) {
            throw new UserException(String.format("TagAdmin collection '%d' not found or not active!", collectionId));
        }
    }
}

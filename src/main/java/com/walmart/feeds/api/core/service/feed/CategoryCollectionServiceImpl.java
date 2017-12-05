package com.walmart.feeds.api.core.service.feed;

/**
 * Created by vn0gshm on 04/12/17.
 */

import com.walmart.feeds.api.client.categoryCollection.CategoryClient;
import com.walmart.feeds.api.client.tagadmin.TagAdmimCollectionClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryCollectionServiceImpl implements CategoryCollectionService {

    @Autowired
    TagAdmimCollectionClient tagAdmimCollectionClient;

    @Override
    public boolean validateTaxonomy(String name) {

        CategoryClient categoryClient = tagAdmimCollectionClient.findByCategoryName(name);
        if (categoryClient == null || !categoryClient.isStatus() == true) {
            return false;
        }
        return true;
    }
}



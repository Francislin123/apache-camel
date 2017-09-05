package com.walmart.feeds.api.unit.core.utils;

import com.walmart.feeds.api.core.utils.MapperUtil;
import com.walmart.feeds.api.resources.blacklist.request.TaxonomyBlacklistMappingRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class MapperUtilsTest {

    @Test
    public void testMappingObjToJson(){
        TaxonomyBlacklistMappingRequest mapping = TaxonomyBlacklistMappingRequest.builder().taxonomy("any > taxonomy")
                .owner("partner")
                .build();
        List<TaxonomyBlacklistMappingRequest> list = new ArrayList<>();
        list.add(mapping);

        String json = "[{\"taxonomy\":\"any > taxonomy\",\"owner\":\"partner\"}]";
        assertEquals(json, MapperUtil.getMapsAsJson(list));
    }
}

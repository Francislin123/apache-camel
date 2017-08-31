package com.walmart.feeds.api.unit.core.utils;

import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyBlacklistMapping;
import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyOwner;
import com.walmart.feeds.api.core.utils.MapperUtil;
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
        TaxonomyBlacklistMapping mapping = TaxonomyBlacklistMapping.builder().taxonomy("any > taxonomy")
                .owner(TaxonomyOwner.PARTNER)
                .build();
        List<TaxonomyBlacklistMapping> list = new ArrayList<>();
        list.add(mapping);

        String json = "[{\"taxonomy\":\"any > taxonomy\",\"owner\":\"PARTNER\"}]";
        assertEquals(json, MapperUtil.getMapsAsJson(list));
    }
}

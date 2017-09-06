package com.walmart.feeds.api.core.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walmart.feeds.api.core.exceptions.SystemException;

public class MapperUtil {

    public MapperUtil() {
        // do nothing
    }

    public static <T> String getMapsAsJson(T list){

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.USE_ANNOTATIONS, true);

        try {
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            throw new SystemException("Error to convert mapped fields to json for history.", e);
        }
    }
}

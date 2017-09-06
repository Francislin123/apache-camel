package com.walmart.feeds.api.persistence;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class ElasticSearchComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchComponent.class);
    public static final String PROPERTIES = "properties";

    @Value("${elastic.mapping.sku}")
    public String urlMapping;

    public List<String> getWalmartFields() {

        LOGGER.info("Retrieve walmart fields from Elasticsearch");

        RestTemplate template = new RestTemplate();
        String response = template.getForEntity(urlMapping, String.class).getBody();

        List<String> listFields = new ArrayList<>();

        try {

            JSONObject esMapping = new JSONObject(response);

            JSONObject skuProperties = esMapping.getJSONObject("walmart")
                    .getJSONObject("mappings")
                    .getJSONObject("skus")
                    .getJSONObject(PROPERTIES);

            getInternalFields(skuProperties, "", listFields);

        } catch (JSONException e) {
            LOGGER.error("Json mapper error", e);
        }
        return listFields;
    }

    private void getInternalFields(JSONObject properties, String parent, List<String> listFields) throws JSONException {

        JSONArray fields = properties.names();

        String parentFormat = (parent != null && !parent.trim().isEmpty()) ?
                parent + "." : "";

        for (int i = 0; i < fields.length(); i++) {

            String fieldName = fields.getString(i);
            String hierarchicalFieldName = parentFormat + fieldName;

            JSONObject field = properties.getJSONObject(fieldName);

            if (field.has(PROPERTIES)) {

                JSONObject internalFields = field.getJSONObject(PROPERTIES);
                getInternalFields(internalFields, hierarchicalFieldName, listFields);

            } else {
                listFields.add(hierarchicalFieldName);

            }
        }
    }
}

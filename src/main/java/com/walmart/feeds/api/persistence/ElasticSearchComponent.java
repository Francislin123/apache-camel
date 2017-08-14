package com.walmart.feeds.api.persistence;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component("elasticSearch")
public class ElasticSearchComponent {

    public static final String URL_MAPPING = "http://napsao-qa-nix-feeds-reg-1.qa.vmcommerce.intra:8080/feeds/_mapping/sku";

    public List<String> getWalmartFields() {

        RestTemplate template = new RestTemplate();
        String response = template.getForEntity(URL_MAPPING, String.class).getBody();

        List<String> listFields = new ArrayList<>();

        try {

            JSONObject esMapping = new JSONObject(response);

            JSONObject skuProperties = esMapping.getJSONObject("walmart")
                    .getJSONObject("mappings")
                    .getJSONObject("skus")
                    .getJSONObject("properties");

            getInternalFields(skuProperties, "", listFields);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(listFields);

        return listFields;

    }

    private void getInternalFields(JSONObject properties, String parent, List<String> listFields) throws JSONException {

        JSONArray fields = properties.names();

        parent = (parent != null && !parent.trim().isEmpty()) ?
                parent + "." : "";

        for (int i = 0; i < fields.length(); i++) {

            String fieldName = fields.getString(i);
            String hierarchicalFieldName = parent + fieldName;

            JSONObject field = properties.getJSONObject(fieldName);

            if (field.has("properties")) {

                JSONObject internalFields = field.getJSONObject("properties");
                getInternalFields(internalFields, hierarchicalFieldName, listFields);

            } else {

                listFields.add(hierarchicalFieldName);

            }

        }

    }


    public static void main(String[] args) {

        ElasticSearchComponent component = new ElasticSearchComponent();
        System.out.println(component.getWalmartFields());

    }

}

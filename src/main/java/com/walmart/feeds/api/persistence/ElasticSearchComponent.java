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

        for(int i = 0; i < fields.length(); i++) {

            JSONObject field = properties.getJSONObject(fields.getString(i));

            if (field.has("properties")) {

                JSONObject internalFields = field.getJSONObject("properties");
                getInternalFields(internalFields, fields.getString(i), listFields);

            } else {

                listFields.add(parent + fields.getString(i));

            }

        }

    }


    public static void main(String[] args) {

        ElasticSearchComponent component = new ElasticSearchComponent();
        System.out.println(component.getWalmartFields());

    }

    private static final String MOCK_ARRAY_FIELDS = "{\n" +
            "    \"walmart\": {\n" +
            "        \"mappings\": {\n" +
            "            \"skus\": {\n" +
            "                \"properties\": {\n" +
            "                    \"active\": {\n" +
            "                        \"type\": \"boolean\"\n" +
            "                    },\n" +
            "                    \"brand\": {\n" +
            "                        \"properties\": {\n" +
            "                            \"id\": {\n" +
            "                                \"type\": \"long\"\n" +
            "                            },\n" +
            "                            \"name\": {\n" +
            "                                \"type\": \"text\",\n" +
            "                                \"fields\": {\n" +
            "                                    \"keyword\": {\n" +
            "                                        \"type\": \"keyword\",\n" +
            "                                        \"ignore_above\": 256\n" +
            "                                    }\n" +
            "                                }\n" +
            "                            }\n" +
            "                        }\n" +
            "                    },\n" +
            "                    \"categories\": {\n" +
            "                        \"properties\": {\n" +
            "                            \"active\": {\n" +
            "                                \"type\": \"boolean\"\n" +
            "                            },\n" +
            "                            \"depth\": {\n" +
            "                                \"type\": \"long\"\n" +
            "                            },\n" +
            "                            \"id\": {\n" +
            "                                \"type\": \"long\"\n" +
            "                            },\n" +
            "                            \"name\": {\n" +
            "                                \"type\": \"text\",\n" +
            "                                \"fields\": {\n" +
            "                                    \"keyword\": {\n" +
            "                                        \"type\": \"keyword\",\n" +
            "                                        \"ignore_above\": 256\n" +
            "                                    }\n" +
            "                                }\n" +
            "                            }\n" +
            "                        }\n" +
            "                    },\n" +
            "                    \"gtin\": {\n" +
            "                        \"type\": \"text\",\n" +
            "                        \"fields\": {\n" +
            "                            \"keyword\": {\n" +
            "                                \"type\": \"keyword\",\n" +
            "                                \"ignore_above\": 256\n" +
            "                            }\n" +
            "                        }\n" +
            "                    },\n" +
            "                    \"id\": {\n" +
            "                        \"type\": \"long\"\n" +
            "                    },\n" +
            "                    \"image\": {\n" +
            "                        \"properties\": {\n" +
            "                            \"main\": {\n" +
            "                                \"type\": \"text\",\n" +
            "                                \"fields\": {\n" +
            "                                    \"keyword\": {\n" +
            "                                        \"type\": \"keyword\",\n" +
            "                                        \"ignore_above\": 256\n" +
            "                                    }\n" +
            "                                }\n" +
            "                            },\n" +
            "                            \"thumb\": {\n" +
            "                                \"type\": \"text\",\n" +
            "                                \"fields\": {\n" +
            "                                    \"keyword\": {\n" +
            "                                        \"type\": \"keyword\",\n" +
            "                                        \"ignore_above\": 256\n" +
            "                                    }\n" +
            "                                }\n" +
            "                            }\n" +
            "                        }\n" +
            "                    },\n" +
            "                    \"lastUpdate\": {\n" +
            "                        \"type\": \"text\",\n" +
            "                        \"fields\": {\n" +
            "                            \"keyword\": {\n" +
            "                                \"type\": \"keyword\",\n" +
            "                                \"ignore_above\": 256\n" +
            "                            }\n" +
            "                        }\n" +
            "                    },\n" +
            "                    \"offers\": {\n" +
            "                        \"properties\": {\n" +
            "                            \"active\": {\n" +
            "                                \"type\": \"boolean\"\n" +
            "                            },\n" +
            "                            \"listprice\": {\n" +
            "                                \"type\": \"long\"\n" +
            "                            },\n" +
            "                            \"price\": {\n" +
            "                                \"type\": \"float\"\n" +
            "                            },\n" +
            "                            \"quantity\": {\n" +
            "                                \"type\": \"long\"\n" +
            "                            },\n" +
            "                            \"seller\": {\n" +
            "                                \"properties\": {\n" +
            "                                    \"id\": {\n" +
            "                                        \"type\": \"long\"\n" +
            "                                    },\n" +
            "                                    \"name\": {\n" +
            "                                        \"type\": \"text\",\n" +
            "                                        \"fields\": {\n" +
            "                                            \"keyword\": {\n" +
            "                                                \"type\": \"keyword\",\n" +
            "                                                \"ignore_above\": 256\n" +
            "                                            }\n" +
            "                                        }\n" +
            "                                    }\n" +
            "                                }\n" +
            "                            }\n" +
            "                        }\n" +
            "                    },\n" +
            "                    \"product\": {\n" +
            "                        \"properties\": {\n" +
            "                            \"active\": {\n" +
            "                                \"type\": \"boolean\"\n" +
            "                            },\n" +
            "                            \"id\": {\n" +
            "                                \"type\": \"text\",\n" +
            "                                \"fields\": {\n" +
            "                                    \"keyword\": {\n" +
            "                                        \"type\": \"keyword\",\n" +
            "                                        \"ignore_above\": 256\n" +
            "                                    }\n" +
            "                                }\n" +
            "                            },\n" +
            "                            \"image\": {\n" +
            "                                \"properties\": {\n" +
            "                                    \"main\": {\n" +
            "                                        \"type\": \"text\",\n" +
            "                                        \"fields\": {\n" +
            "                                            \"keyword\": {\n" +
            "                                                \"type\": \"keyword\",\n" +
            "                                                \"ignore_above\": 256\n" +
            "                                            }\n" +
            "                                        }\n" +
            "                                    },\n" +
            "                                    \"thumb\": {\n" +
            "                                        \"type\": \"text\",\n" +
            "                                        \"fields\": {\n" +
            "                                            \"keyword\": {\n" +
            "                                                \"type\": \"keyword\",\n" +
            "                                                \"ignore_above\": 256\n" +
            "                                            }\n" +
            "                                        }\n" +
            "                                    }\n" +
            "                                }\n" +
            "                            },\n" +
            "                            \"title\": {\n" +
            "                                \"type\": \"text\",\n" +
            "                                \"fields\": {\n" +
            "                                    \"keyword\": {\n" +
            "                                        \"type\": \"keyword\",\n" +
            "                                        \"ignore_above\": 256\n" +
            "                                    }\n" +
            "                                }\n" +
            "                            },\n" +
            "                            \"url\": {\n" +
            "                                \"properties\": {\n" +
            "                                    \"mobile\": {\n" +
            "                                        \"type\": \"text\",\n" +
            "                                        \"fields\": {\n" +
            "                                            \"keyword\": {\n" +
            "                                                \"type\": \"keyword\",\n" +
            "                                                \"ignore_above\": 256\n" +
            "                                            }\n" +
            "                                        }\n" +
            "                                    },\n" +
            "                                    \"web\": {\n" +
            "                                        \"type\": \"text\",\n" +
            "                                        \"fields\": {\n" +
            "                                            \"keyword\": {\n" +
            "                                                \"type\": \"keyword\",\n" +
            "                                                \"ignore_above\": 256\n" +
            "                                            }\n" +
            "                                        }\n" +
            "                                    }\n" +
            "                                }\n" +
            "                            }\n" +
            "                        }\n" +
            "                    },\n" +
            "                    \"skuWalmart\": {\n" +
            "                        \"type\": \"text\",\n" +
            "                        \"fields\": {\n" +
            "                            \"keyword\": {\n" +
            "                                \"type\": \"keyword\",\n" +
            "                                \"ignore_above\": 256\n" +
            "                            }\n" +
            "                        }\n" +
            "                    },\n" +
            "                    \"specification\": {\n" +
            "                        \"properties\": {\n" +
            "                            \"dimension\": {\n" +
            "                                \"properties\": {\n" +
            "                                    \"weight\": {\n" +
            "                                        \"type\": \"float\"\n" +
            "                                    }\n" +
            "                                }\n" +
            "                            }\n" +
            "                        }\n" +
            "                    },\n" +
            "                    \"title\": {\n" +
            "                        \"type\": \"text\",\n" +
            "                        \"fields\": {\n" +
            "                            \"keyword\": {\n" +
            "                                \"type\": \"keyword\",\n" +
            "                                \"ignore_above\": 256\n" +
            "                            }\n" +
            "                        }\n" +
            "                    },\n" +
            "                    \"url\": {\n" +
            "                        \"properties\": {\n" +
            "                            \"mobile\": {\n" +
            "                                \"type\": \"text\",\n" +
            "                                \"fields\": {\n" +
            "                                    \"keyword\": {\n" +
            "                                        \"type\": \"keyword\",\n" +
            "                                        \"ignore_above\": 256\n" +
            "                                    }\n" +
            "                                }\n" +
            "                            },\n" +
            "                            \"web\": {\n" +
            "                                \"type\": \"text\",\n" +
            "                                \"fields\": {\n" +
            "                                    \"keyword\": {\n" +
            "                                        \"type\": \"keyword\",\n" +
            "                                        \"ignore_above\": 256\n" +
            "                                    }\n" +
            "                                }\n" +
            "                            }\n" +
            "                        }\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}";
}

package com.walmart.feeds.api.core.persistence.elasticsearch;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Service
public class ElasticSearchServiceImpl implements ElasticSearchService{

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public boolean validateWalmartTaxonomy(String taxonomy){
        if(StringUtils.isEmpty(taxonomy)){
            return false;
        }else{
            Boolean valid = Boolean.TRUE;
            final String[] split = taxonomy.split(">");
            int i = 0;
            for (String name: split) {
                valid &= validateWalmartTaxonomy(name, i);
                i++;
            }
            return valid;
        }
    }

    @Override
    public List<String> getSkuFieldsMapping() {

        Map<String, Map> mapping = elasticsearchTemplate.getMapping("skus", "sku");

        List<String> listFields = new ArrayList<>();
        processMap(mapping.get("properties"), "", listFields);

        return listFields;

    }

    private void processMap(Map<String, Map> mapping, String parent, List<String> listFields) {

        parent = (parent != null && !parent.trim().isEmpty()) ?
                parent + "." : "";

        for(String key: mapping.keySet()) {

            String hierarchicalFieldName = parent + key;

            if(key.equals("properties")) {

                processMap(mapping.get("properties"), hierarchicalFieldName, listFields);

            } else if (mapping.get(key).containsKey("properties")) {

                processMap((Map) mapping.get(key).get("properties"), hierarchicalFieldName, listFields);

            } else if (!key.equals("type")) {

                listFields.add(hierarchicalFieldName);

            }

        }

    }

    private boolean validateWalmartTaxonomy(String categoryName, Integer depth) {
        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
        queryBuilder.must(matchQuery("categories.name", categoryName));
        queryBuilder.must(matchQuery("categories.depth", depth));
        NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery("categories", queryBuilder);

        return elasticsearchTemplate.count(new NativeSearchQueryBuilder()
                .withIndices("skus")
                .withTypes("sku")
                .withQuery(nestedQueryBuilder)
                .build()) > 0;
    }
}

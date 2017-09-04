package com.walmart.feeds.api.core.persistence.elasticsearch;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

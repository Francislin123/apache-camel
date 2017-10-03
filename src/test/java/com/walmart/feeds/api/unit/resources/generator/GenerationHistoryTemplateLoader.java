package com.walmart.feeds.api.unit.resources.generator;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.walmart.feeds.api.resources.generator.request.GenerationHistoryRequest;

import java.time.LocalDateTime;

public class GenerationHistoryTemplateLoader implements TemplateLoader {

    public static final String GENERATION_HISTORY = "generation-history";
    public static final String GENERATION_HISTORY_NO_SKUS = "generation-history-no-skus";
    public static final String GENERATION_HISTORY_NO_FILENAME= "generation-history-no-filename";
    public static final String GENERATION_HISTORY_NO_FEED= "generation-history-no-feed";
    public static final String GENERATION_HISTORY_NO_PARTNER= "generation-history-no-partner";

    @Override
    public void load() {

        Fixture.of(GenerationHistoryRequest.class).addTemplate(GENERATION_HISTORY, new Rule() {{
            add("partnerSlug", "zoom");
            add("feedSlug", "zoom-cpc");
            add("fileName", "2017-09-28_11-05-30-zoom-cpc.xml");
            add("generationDate", LocalDateTime.now());
            add("totalSkus", 1000000);
        }})
        .addTemplate(GENERATION_HISTORY_NO_SKUS).inherits(GENERATION_HISTORY, new Rule() {{
            add("totalSkus", 0);
        }})
        .addTemplate(GENERATION_HISTORY_NO_FEED).inherits(GENERATION_HISTORY, new Rule() {{
            add("feedSlug", null);
        }})
        .addTemplate(GENERATION_HISTORY_NO_PARTNER).inherits(GENERATION_HISTORY, new Rule() {{
            add("partnerSlug", null);
        }})
        .addTemplate(GENERATION_HISTORY_NO_FILENAME).inherits(GENERATION_HISTORY, new Rule() {{
            add("fileName", null);
        }});

    }

}

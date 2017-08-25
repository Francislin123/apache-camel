package com.walmart.feeds.api.unit.resources.blacklist.taxonomy;


import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyBlacklistEntity;
import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyBlacklistMapping;
import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyOwner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TaxonomyBlacklistTemplate implements TemplateLoader{

    @Override
    public void load() {
        Set<TaxonomyBlacklistMapping> list = new HashSet<>();

        Fixture.of(TaxonomyBlacklistMapping.class).addTemplate("tax-bl-mapping-entity", new Rule() {{
            add("taxonomy", "any > taxonomy");
            add("owner", TaxonomyOwner.PARTNER);
        }});

        list.add(Fixture.from(TaxonomyBlacklistMapping.class).gimme("tax-bl-mapping-entity"));

        Fixture.of(TaxonomyBlacklistEntity.class).addTemplate("tax-bl-entity", new Rule() {{
            add("name", "any name");
            add("slug", "any-name");
            add("list", list);
        }});

    }
}

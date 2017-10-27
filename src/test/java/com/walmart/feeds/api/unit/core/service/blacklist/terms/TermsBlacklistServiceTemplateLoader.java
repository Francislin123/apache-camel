package com.walmart.feeds.api.unit.core.service.blacklist.terms;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.walmart.feeds.api.core.repository.blacklist.model.TermsBlacklistEntity;

import java.util.HashSet;
import java.util.Set;

public class TermsBlacklistServiceTemplateLoader implements TemplateLoader {

    public static final String TERMS_BLACK_LIST = "terms-black-list-entity";
    public static final String TERMS_BLACK_LIST_UPDATE_NAME= "terms-black-list-update-name";

    @Override
    public void load() {

        Fixture.of(TermsBlacklistEntity.class).addTemplate(TERMS_BLACK_LIST, new Rule() {{

            Set<String> list = new HashSet<>();
            list.add("bullet");

            add("name", "Facebook-terms-blacklist");
            add("slug", "facebook-terms-blacklist");
            add("list", list);
        }});

        Fixture.of(TermsBlacklistEntity.class).addTemplate(TERMS_BLACK_LIST_UPDATE_NAME, new Rule() {{

            Set<String> list = new HashSet<>();
            list.add("bullet");
            list.add("arms");

            add("name", "Google-terms-blacklist");
            add("slug", "facebook-terms-blacklist");
            add("list", list);
        }});
    }
}

package com.walmart.feeds.api.unit.resources.blacklist.terms;


import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.walmart.feeds.api.core.repository.blacklist.model.TermsBlacklistEntity;
import com.walmart.feeds.api.resources.blacklist.request.TermsBlacklistRequest;

import java.util.HashSet;
import java.util.Set;

public class TermsBlackListTemplateLoader implements TemplateLoader {

    public static final String TERMS_BLACKLIST_REQUEST_VALID = "terms_black_valid";
    public static final String TERMS_BLACKLIST_REQUEST_INVALID = "terms_black_invalid";
    public static final String TERMS_BLACKLIST_REQUEST_NULL = "terms_black_null";
    public static final String TERMS_BLACKLIST_REQUEST_EMPTY_NAME = "terms_black_empty_name";

    @Override
    public void load() {
        // ---------------------------------------------------------------------------------------------------------- //

        Set<String> valid_list = new HashSet<>();
        valid_list.add("bullet");
        valid_list.add("beverage");

        Fixture.of(TermsBlacklistEntity.class).addTemplate("terms_black_valid", new Rule() {{
            add("list", valid_list);
        }});

        Set<String> invalid_list = new HashSet<>();
        invalid_list.add(null);

        Fixture.of(TermsBlacklistEntity.class).addTemplate("terms_black_invalid", new Rule() {{
            add("list", invalid_list);
        }});

        Set<String> terms_black_null = new HashSet<>();
        invalid_list.add(null);

        Fixture.of(TermsBlacklistEntity.class).addTemplate("terms_black_null", new Rule() {{
            add("list", terms_black_null);
        }});

        // ---------------------------------------------------------------------------------------------------------- //

        Fixture.of(TermsBlacklistRequest.class).addTemplate(TERMS_BLACKLIST_REQUEST_VALID, new Rule() {{
            add("name", "bullet");
            add("list", valid_list);
        }});

        Fixture.of(TermsBlacklistRequest.class).addTemplate(TERMS_BLACKLIST_REQUEST_INVALID, new Rule() {{
            add("name", "bullet");
            add("list", invalid_list);
        }});

        Fixture.of(TermsBlacklistRequest.class).addTemplate(TERMS_BLACKLIST_REQUEST_NULL, new Rule() {{
            add("name", "bullet");
            add("list", terms_black_null);
        }});

        Fixture.of(TermsBlacklistRequest.class).addTemplate(TERMS_BLACKLIST_REQUEST_EMPTY_NAME, new Rule() {{
            add("name", "");
            add("list", valid_list);
        }});

        // ---------------------------------------------------------------------------------------------------------- //
    }
}

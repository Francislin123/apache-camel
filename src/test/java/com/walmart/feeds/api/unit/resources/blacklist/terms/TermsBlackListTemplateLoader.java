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
    public static final String TERMS_BLACKLIST_REQUEST_EMPTY_LIST = "terms_black_empty_name";

    @Override
    public void load() {

        // ---------------------------------------------------------------------------------------------------------- //

        Set<String> validList = new HashSet<>();
        validList.add("bullet");
        validList.add("beverage");

        Fixture.of(TermsBlacklistEntity.class).addTemplate("terms_black_valid", new Rule() {{
            add("list", validList);
        }});

        Set<String> invalidList = new HashSet<>();
        invalidList.add(null);

        Fixture.of(TermsBlacklistEntity.class).addTemplate("terms_black_invalid", new Rule() {{
            add("list", invalidList);
        }});

        Set<String> terms_black_null = new HashSet<>();
        invalidList.add(null);

        Fixture.of(TermsBlacklistEntity.class).addTemplate("terms_black_null", new Rule() {{
            add("list", terms_black_null);
        }});

        // ---------------------------------- Test Create Terms Blacklist begin --------------------------------------//

        Fixture.of(TermsBlacklistRequest.class).addTemplate(TERMS_BLACKLIST_REQUEST_VALID, new Rule() {{
            add("name", "Facebook Terms Blacklist");
            add("list", validList);
        }});

        Fixture.of(TermsBlacklistRequest.class).addTemplate(TERMS_BLACKLIST_REQUEST_INVALID, new Rule() {{
            add("name", "Facebook Terms Blacklist");
            add("list", invalidList);
        }});

        Fixture.of(TermsBlacklistRequest.class).addTemplate(TERMS_BLACKLIST_REQUEST_NULL, new Rule() {{
            add("name", "Facebook Terms Blacklist");
            add("list", terms_black_null);
        }});

        Fixture.of(TermsBlacklistRequest.class).addTemplate(TERMS_BLACKLIST_REQUEST_EMPTY_NAME, new Rule() {{
            add("name", "");
            add("list", validList);
        }});

        // ---------------------------------- Test Create Terms Blacklist end ----------------------------------------//

        // ---------------------------------- Test Create Terms Blacklist begin --------------------------------------//

        Fixture.of(TermsBlacklistRequest.class).addTemplate(TERMS_BLACKLIST_REQUEST_EMPTY_LIST, new Rule() {{
            add("name", "Facebook Terms Blacklist");
        }});

        // ---------------------------------- Test Create Terms Blacklist end ----------------------------------------//
    }
}

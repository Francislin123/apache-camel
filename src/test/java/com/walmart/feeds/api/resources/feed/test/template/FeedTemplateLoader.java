package com.walmart.feeds.api.resources.feed.test.template;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.walmart.feeds.api.core.repository.feed.model.FeedType;
import com.walmart.feeds.api.resources.feed.request.FeedNotificationData;
import com.walmart.feeds.api.resources.feed.request.FeedRequest;
import com.walmart.feeds.api.resources.feed.request.UTM;

public class FeedTemplateLoader implements TemplateLoader {
    @Override
    public void load() {
        Fixture.of(FeedNotificationData.class).addTemplate("valid-notification-api", new Rule() {{
            add("method", "api");
            add("format", "json");
            add("url", "http://localhost:8080/notification");
        }});

        Fixture.of(FeedNotificationData.class).addTemplate("valid-notification-file", new Rule() {{
            add("method", "file");
            add("format", "xml");
        }});

        Fixture.of(UTM.class).addTemplate("utm_valid", new Rule() {{
            add("type", random("utm_source", "utm_campaign", "utm_blabla", "utm_term", "utm_content", "utm_medium"));
            add("value", random(String.class, "asdasd", "qweqwe", "12312312"));
        }});

        Fixture.of(FeedRequest.class).addTemplate("feed-request-generic-valid", new Rule() {{
            add("name", "Feed WM Test");
            add("reference", "feed_test");
            add("utms", Fixture.from(UTM.class).gimme(3, "utm_valid"));
        }});

        Fixture.of(FeedRequest.class).addTemplate("feed-full-api-valid").inherits("feed-request-generic-valid", new Rule() {{
            add("type", FeedType.FULL.getType());
            FeedNotificationData notification = Fixture.from(FeedNotificationData.class).gimme("valid-notification-api");
            add("notification", notification);
        }});

        Fixture.of(FeedRequest.class).addTemplate("feed-full-file-valid").inherits("feed-request-generic-valid", new Rule() {{
            add("type", FeedType.FULL.getType());
            FeedNotificationData notification = Fixture.from(FeedNotificationData.class).gimme("valid-notification-file");
            add("notification", notification);
        }});

        Fixture.of(FeedRequest.class).addTemplate("feed-full-without-name").inherits("feed-request-generic-valid", new Rule() {{
            add("name", null);
            add("type", FeedType.FULL.getType());
            FeedNotificationData notification = Fixture.from(FeedNotificationData.class).gimme("valid-notification-file");
            add("notification", notification);
        }});
    }
}

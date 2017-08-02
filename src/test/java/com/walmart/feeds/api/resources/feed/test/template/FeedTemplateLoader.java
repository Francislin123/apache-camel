package com.walmart.feeds.api.resources.feed.test.template;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.walmart.feeds.api.core.repository.feed.model.FeedType;
import com.walmart.feeds.api.core.service.feed.model.FeedNotificationDataTO;
import com.walmart.feeds.api.core.service.feed.model.FeedTO;
import com.walmart.feeds.api.resources.feed.request.FeedNotificationData;
import com.walmart.feeds.api.resources.feed.request.FeedRequest;

import java.util.HashMap;
import java.util.Map;

public class FeedTemplateLoader implements TemplateLoader {
    @Override
    public void load() {
        Map<String, String> utms = new HashMap<>();
        utms.put("utm_source", "zoom");
        Fixture.of(FeedNotificationData.class).addTemplate("valid-notification-api", new Rule() {{
            add("method", "api");
            add("format", "json");
            add("url", "http://localhost:8080/notification");
        }});

        Fixture.of(FeedNotificationData.class).addTemplate("valid-notification-file", new Rule() {{
            add("method", "file");
            add("format", "xml");
        }});



        Fixture.of(FeedRequest.class).addTemplate("feed-request-generic-valid", new Rule() {{
            add("name", "Feed WM Test");
            add("reference", "feed_test");
            add("utms", utms);
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

        Fixture.of(FeedNotificationDataTO.class).addTemplate("valid-notification-to-api", new Rule() {{
            add("method", "api");
            add("format", "json");
            add("url", "http://localhost:8080/notification");
        }});

        Fixture.of(FeedTO.class).addTemplate("feed-to-full-api-valid", new Rule() {{
            add("name", "Feed WM Test");
            add("reference", "feed_test");
            add("utms", utms);
            add("type", FeedType.FULL);
            FeedNotificationDataTO notification = Fixture.from(FeedNotificationDataTO.class).gimme("valid-notification-to-api");
            add("notificationData", notification);
        }});
    }
}
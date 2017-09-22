package com.walmart.feeds.api.client.gossip;

import com.walmart.feeds.api.core.repository.maillog.model.GossipEntity;
import feign.Headers;
import feign.RequestLine;


public interface GossipClient {

    @RequestLine("POST /rest/mail")
    @Headers({"Content-Type: application/json", "Cache-Control: no-cache"})
    void sendEmail(GossipEntity gossipEntity);
}

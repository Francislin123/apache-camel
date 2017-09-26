package com.walmart.feeds.api;

import com.walmart.feeds.api.client.gossip.GossipClient;
import feign.Feign;
import feign.Logger;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GossipConfiguration {

    @Value("${gossip.username}")
    private String gossipUser;

    @Value("${gossip.password}")
    private String gossipPassword;

    @Value("${gossip.uri}")
    private String uri;

    @Bean
    public GossipClient gossipClient() {
        return Feign.builder()
                .client(new OkHttpClient())
                .decoder(new JacksonDecoder())
                .encoder(new JacksonEncoder())
                .logger(new Slf4jLogger(GossipClient.class))
                .logLevel(Logger.Level.FULL)
                .requestInterceptor(new BasicAuthRequestInterceptor(gossipUser, gossipPassword))
                .target(GossipClient.class, uri);
    }
}

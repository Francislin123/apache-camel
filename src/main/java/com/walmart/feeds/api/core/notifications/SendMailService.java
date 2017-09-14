package com.walmart.feeds.api.core.notifications;

public interface SendMailService {
    void sendMail(FeedErrorNotification feedErrorNotification);
}

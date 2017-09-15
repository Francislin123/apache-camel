package com.walmart.feeds.api.core.notifications;

public interface SendMailService {
    void sendMail(String feedSlug, String partnerSlug, String message);
}

package com.walmart.feeds.api.core.service.mail;


import com.walmart.feeds.api.core.repository.mailconf.model.MailConfEntity;

public interface MailConfService {

    MailConfEntity fetchBySlug(String slug);
}

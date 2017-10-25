package com.walmart.feeds.api.core.service.mail;

import com.walmart.feeds.api.core.exceptions.SystemException;
import com.walmart.feeds.api.core.repository.mailconf.MailConfRepository;
import com.walmart.feeds.api.core.repository.mailconf.model.MailConfEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailConfServiceImpl implements MailConfService {

    @Autowired
    private MailConfRepository mailConfRepository;

    @Override
    public MailConfEntity fetchBySlug(String slug) {
        return mailConfRepository.findBySlug(slug).orElseThrow(() -> new SystemException("There's no mail configuration setup"));
    }
}

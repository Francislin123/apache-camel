package com.walmart.feeds.api.core.service.maillog;

import com.walmart.feeds.api.core.repository.maillog.MailLogRepository;
import com.walmart.feeds.api.core.repository.maillog.model.MailLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailLogServiceImpl implements MailLogService {

    @Autowired
    private MailLogRepository mailLogRepository;

    @Override
    public void log(MailLogEntity mailLogEntity) {
        mailLogRepository.save(mailLogEntity);
    }
}

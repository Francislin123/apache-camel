package com.walmart.feeds.api.core.service.maillog;

import com.walmart.feeds.api.core.repository.maillog.model.MailLogEntity;

/**
 * Created by vn0y942 on 14/09/17.
 */
public interface MailLogService {

    void log(MailLogEntity mailLogEntity);
}

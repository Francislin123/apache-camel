package com.walmart.feeds.api.core.service.template;

import com.walmart.feeds.api.core.repository.template.model.TemplateEntity;

public interface TemplateService {
    TemplateEntity fetchBySlug(String slug);
}

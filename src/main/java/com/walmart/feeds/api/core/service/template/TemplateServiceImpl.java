package com.walmart.feeds.api.core.service.template;

import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.repository.template.TemplateRepository;
import com.walmart.feeds.api.core.repository.template.model.TemplateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private TemplateRepository templateRepository;

    @Override
    public TemplateEntity fetchBySlug(String slug) {
        return templateRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Template '%s' not found", slug)));
    }

}

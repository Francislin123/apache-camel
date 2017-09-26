package com.walmart.feeds.api.unit.core.service.template;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.repository.template.TemplateRepository;
import com.walmart.feeds.api.core.repository.template.model.TemplateEntity;
import com.walmart.feeds.api.core.service.template.TemplateService;
import com.walmart.feeds.api.core.service.template.TemplateServiceImpl;
import com.walmart.feeds.api.resources.infrastructure.FeedsAdminAPIExceptionHandler;
import com.walmart.feeds.api.resources.template.TemplateController;
import com.walmart.feeds.api.unit.core.service.feed.FeedEntityTemplateLoader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class TemplateServiceTest {

    @Mock
    private TemplateRepository templateRepository;

    @InjectMocks
    private TemplateService templateService = new TemplateServiceImpl();

    @BeforeClass
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.core.service.feed");
    }

    @Test
    public void testFetchTemplate() throws Exception {

        when(templateRepository.findBySlug("default-template"))
                .thenReturn(Optional.of(getTemplate()));

        TemplateEntity templateEntity = templateService.fetchBySlug("default-template");

        assertEquals("Default Template", templateEntity.getName());
        assertEquals("default-template", templateEntity.getSlug());
        assertEquals(">", templateEntity.getSeparator());

    }

    @Test(expected = EntityNotFoundException.class)
    public void testFetchTemplateWhenNotExists() throws Exception {

        when(templateRepository.findBySlug("default-template"))
                .thenReturn(Optional.empty());

        templateService.fetchBySlug("default-template");

    }

    public TemplateEntity getTemplate() {
        return Fixture.from(TemplateEntity.class).gimme(FeedEntityTemplateLoader.TEMPLATE_ENTITY);
    }
}

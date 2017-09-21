package com.walmart.feeds.api.unit.resources.template;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.repository.template.model.TemplateEntity;
import com.walmart.feeds.api.core.service.template.TemplateService;
import com.walmart.feeds.api.resources.infrastructure.FeedsAdminAPIExceptionHandler;
import com.walmart.feeds.api.resources.template.TemplateController;
import com.walmart.feeds.api.unit.core.service.feed.FeedEntityTemplateLoader;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.results.ResultMatchers;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class TemplateControllerTest {

    @Mock
    private TemplateService templateService;

    @InjectMocks
    private TemplateController templateController;

    private MockMvc mockMvc;

    @BeforeClass
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.core.service.feed");
    }

    @Before
    public void init() {
        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.resources.taxonomy.test.template");
        mockMvc = MockMvcBuilders.standaloneSetup(templateController)
                .setControllerAdvice(new FeedsAdminAPIExceptionHandler())
                .build();
    }

    @Test
    public void testFetchTemplate() throws Exception {

        when(templateService.fetchBySlug("default-template"))
                .thenReturn(getTemplate());

        mockMvc.perform(MockMvcRequestBuilders.get(TemplateController.V1_TEMPLATE + "/default-template"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Default Template"));

    }

    @Test
    public void testFetchTemplateWhenNotExists() throws Exception {

        when(templateService.fetchBySlug("default-template"))
                .thenThrow(EntityNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get(TemplateController.V1_TEMPLATE + "/default-template"))
                .andExpect(status().isNotFound());

    }

    public TemplateEntity getTemplate() {
        return Fixture.from(TemplateEntity.class).gimme(FeedEntityTemplateLoader.TEMPLATE_ENTITY);
    }
}

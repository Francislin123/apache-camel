package com.walmart.feeds.api.unit.resources.generator;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.walmart.feeds.api.core.repository.generator.GenerationHistoryRepository;
import com.walmart.feeds.api.core.repository.generator.model.GenerationHistoryEntity;
import com.walmart.feeds.api.core.utils.MapperUtil;
import com.walmart.feeds.api.resources.generator.GenerationHistoryController;
import com.walmart.feeds.api.resources.generator.request.GenerationHistoryRequest;
import com.walmart.feeds.api.resources.infrastructure.FeedsAdminAPIExceptionHandler;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(MockitoJUnitRunner.class)
public class GenerationHistoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GenerationHistoryRepository historyRepository;

    @InjectMocks
    private GenerationHistoryController historyController = new GenerationHistoryController();

    @BeforeClass
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.resources.generator");
    }

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(historyController)
                .setControllerAdvice(new FeedsAdminAPIExceptionHandler())
                .build();
    }

    @Test
    public void testSave() throws Exception {

        GenerationHistoryRequest request = Fixture.from(GenerationHistoryRequest.class).gimme(GenerationHistoryTemplateLoader.GENERATION_HISTORY);

        mockMvc.perform(post(GenerationHistoryController.GENERATION_HISTORY)
                    .contentType(MediaType.APPLICATION_JSON_UTF8).content(MapperUtil.getMapsAsJson(request)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Mockito.verify(historyRepository).save(any(GenerationHistoryEntity.class));
    }

    @Test
    public void testSaveWhenTotalSkusIsZero() throws Exception {

        GenerationHistoryRequest request = Fixture.from(GenerationHistoryRequest.class).gimme(GenerationHistoryTemplateLoader.GENERATION_HISTORY_NO_SKUS);

        mockMvc.perform(post(GenerationHistoryController.GENERATION_HISTORY)
                    .contentType(MediaType.APPLICATION_JSON_UTF8).content(MapperUtil.getMapsAsJson(request)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(historyRepository, never()).save(any(GenerationHistoryEntity.class));
    }

    @Test
    public void testSaveWhenFileNameIsNull() throws Exception {

        GenerationHistoryRequest request = Fixture.from(GenerationHistoryRequest.class).gimme(GenerationHistoryTemplateLoader.GENERATION_HISTORY_NO_FILENAME);

        mockMvc.perform(post(GenerationHistoryController.GENERATION_HISTORY)
                    .contentType(MediaType.APPLICATION_JSON_UTF8).content(MapperUtil.getMapsAsJson(request)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(historyRepository, never()).save(any(GenerationHistoryEntity.class));
    }

    @Test
    public void testSaveWhenFeedIsNull() throws Exception {

        GenerationHistoryRequest request = Fixture.from(GenerationHistoryRequest.class).gimme(GenerationHistoryTemplateLoader.GENERATION_HISTORY_NO_FEED);

        mockMvc.perform(post(GenerationHistoryController.GENERATION_HISTORY)
                    .contentType(MediaType.APPLICATION_JSON_UTF8).content(MapperUtil.getMapsAsJson(request)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(historyRepository, never()).save(any(GenerationHistoryEntity.class));
    }

    @Test
    public void testSaveWhenPartnerIsNull() throws Exception {

        GenerationHistoryRequest request = Fixture.from(GenerationHistoryRequest.class).gimme(GenerationHistoryTemplateLoader.GENERATION_HISTORY_NO_PARTNER);

        mockMvc.perform(post(GenerationHistoryController.GENERATION_HISTORY)
                    .contentType(MediaType.APPLICATION_JSON_UTF8).content(MapperUtil.getMapsAsJson(request)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(historyRepository, never()).save(any(GenerationHistoryEntity.class));
    }

}

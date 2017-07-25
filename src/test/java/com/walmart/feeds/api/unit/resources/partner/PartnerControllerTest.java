package com.walmart.feeds.api.unit.resources.partner;

import com.walmart.feeds.api.core.service.partner.PartnerService;
import com.walmart.feeds.api.resources.partner.PartnerController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class PartnerControllerTest {

    private Logger logger = LoggerFactory.getLogger(PartnerControllerTest.class);

    @Autowired
    private PartnerController controller;

    @MockBean
    private PartnerService partnerService;

    @Autowired
    private MockMvc mvc;

    @Value("classpath:partner/partnerRequest.json")
    private Resource partnerResource;

    @Value("classpath:partner/invalidPartnerRequest.json")
    private Resource invalidPartnerResource;

    @Test
    public void testCreateNewPartner() throws Exception {
        String partnerRequestJson = getPartnerRequestJson(partnerResource);
        logger.info(partnerRequestJson);
        this.mvc.perform(post("/partners")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(partnerRequestJson))
            .andExpect(status().isCreated());

    }

    @Test
    public void testCreateNewPartnerShouldFailWithEmptyBody() throws Exception {
        String partnerRequestJson = "";
        logger.info(partnerRequestJson);
        this.mvc.perform(post("/partners")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(partnerRequestJson))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testCreateNewPartnerShouldFailWithInvalidBody() throws Exception {
        String partnerRequestJson = getPartnerRequestJson(invalidPartnerResource);
        logger.info("Body:\n{}", partnerRequestJson);
        this.mvc.perform(post("/partners")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(partnerRequestJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    private String getPartnerRequestJson(Resource resource) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths
                .get(resource.getFile().getPath()));
        return new String(bytes);
    }

}

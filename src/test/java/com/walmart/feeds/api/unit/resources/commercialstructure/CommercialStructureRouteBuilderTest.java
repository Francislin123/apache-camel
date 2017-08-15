package com.walmart.feeds.api.unit.resources.commercialstructure;

import com.walmart.feeds.api.core.repository.commercialstructure.CommercialStructureRepository;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureEntity;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.core.service.partner.PartnerService;
import com.walmart.feeds.api.resources.camel.CommercialStructureProcessor;
import com.walmart.feeds.api.resources.camel.CommercialStructureRouteBuilder;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.MockEndpoints;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

@MockEndpoints
@RunWith(MockitoJUnitRunner.class)
public class CommercialStructureRouteBuilderTest extends CamelTestSupport {

    @Produce(uri = "direct:test")
    private ProducerTemplate producer;

    @EndpointInject(uri = "mock:direct:end")
    private MockEndpoint endpoint;

    @Mock
    private PartnerService partnerService;

    @Mock
    private CommercialStructureRepository commercialStructureRepository;

    @InjectMocks
    private CommercialStructureProcessor commercialStructureProcessor = new CommercialStructureProcessor();

    @Test
    public void testSendFile() throws Exception {

        Mockito.when(partnerService.findBySlug("test")).thenReturn(PartnerEntity.builder().slug("test").build());

        String csv = "id;wm;partner\n123;Informatica;Computadores\n124;Smartphones;Telefonia";
        ByteArrayInputStream in = new ByteArrayInputStream(csv.getBytes());
        Map<String, Object> headers = getHeaders();

        producer.sendBodyAndHeaders(in, headers);

        endpoint.assertIsSatisfied();

    }

    @Test
    public void testSendFileWhenHasEmptyColumn() throws Exception {

        Mockito.when(partnerService.findBySlug("test")).thenReturn(PartnerEntity.builder().slug("test").build());

        String csv = "id;wm;partner\n123;;Computadores\n124;Smartphones;Telefonia";
        ByteArrayInputStream in = new ByteArrayInputStream(csv.getBytes());
        Map<String, Object> headers = getHeaders();

        producer.sendBodyAndHeaders(in, headers);

        endpoint.assertIsSatisfied();

    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        RoutesBuilder routeBuilder = new CommercialStructureRouteBuilder(producer.getCamelContext(), commercialStructureProcessor);
        return routeBuilder;
    }

    private Map<String, Object> getHeaders() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("partnerSlug", "test");
        headers.put("archiveName", "file");
        return headers;
    }

}

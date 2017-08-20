package com.walmart.feeds.api.unit.resources.taxonomy;

import com.walmart.feeds.api.core.exceptions.UserException;
import com.walmart.feeds.api.resources.camel.TaxonomyMappingBindy;
import com.walmart.feeds.api.resources.camel.PartnerTaxonomyRouteBuilder;
import org.apache.camel.*;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class PartnerTaxonomyRouteBuilderTest extends CamelTestSupport {

    @Produce(uri = PartnerTaxonomyRouteBuilder.VALIDATE_FILE_ROUTE)
    private ProducerTemplate producer;

    @EndpointInject(uri = "mock:direct:failToLoadCsFile")
    private MockEndpoint failEndpoint;

    private ArgumentCaptor<Exchange> argumentCaptor;

    @Before
    public void init() {
        argumentCaptor = ArgumentCaptor.forClass(Exchange.class);
    }

    @Test
    public void testSendFile() throws Exception {

        StringBuilder csvBuilder = new StringBuilder()
                .append("id;wm;partner\n")
                .append("123;Informatica;Computadores\n")
                .append("124;Smartphones;Telefonia");
        ByteArrayInputStream in = new ByteArrayInputStream(csvBuilder.toString().getBytes());
        Map<String, Object> headers = getHeaders();

        producer.sendBodyAndHeaders(in, headers);

        Object body = argumentCaptor.getValue().getIn().getBody();

        if(body instanceof List) {

            List<TaxonomyMappingBindy> taxonomies = (List) body;

            assertEquals(2, taxonomies.size());
            assertEquals("Computadores", taxonomies.get(0).getWalmartTaxonomy());

        } else {
            fail("Camel body should be a List");
        }
    }

    @Test
    public void testSendFileWhenHasEmptyColumn() throws Exception {

        StringBuilder csvBuilder = new StringBuilder()
                .append("id;wm;partner\n")
                .append("123;;Computadores\n")
                .append("124;Smartphones;Telefonia");
        ByteArrayInputStream in = new ByteArrayInputStream(csvBuilder.toString().getBytes());
        Map<String, Object> headers = getHeaders();

        try {

            producer.sendBodyAndHeaders(in, headers);

        } catch (CamelExecutionException e) {

            System.err.println("CAUSE >> " + e.getCause());
            assertTrue(e.getCause() instanceof UserException);
            assertEquals("The mandatory field defined at the position 2 is empty for the line: 1", e.getCause().getMessage());

        }

    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {

        CamelContext camelContext = producer.getCamelContext();
        camelContext.addEndpoint("direct:failToLoadCsFile", failEndpoint);

        RoutesBuilder routeBuilder = new PartnerTaxonomyRouteBuilder(camelContext);
        return routeBuilder;

    }

    @Override
    public String isMockEndpoints() {
        return "*";
    }

    private Map<String, Object> getHeaders() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("partnerSlug", "test");
        headers.put("archiveName", "file");
        return headers;
    }

}

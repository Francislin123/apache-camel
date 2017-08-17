package com.walmart.feeds.api.resources.camel;

import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.exceptions.UserException;
import com.walmart.feeds.api.core.repository.commercialstructure.CommercialStructureAssociationRepository;
import com.walmart.feeds.api.core.repository.commercialstructure.CommercialStructureRepository;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureAssociationEntity;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureEntity;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.core.service.partner.PartnerService;
import com.walmart.feeds.api.core.utils.SlugParserUtil;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.ValueBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultThreadPoolFactory;
import org.apache.camel.processor.aggregate.UseLatestAggregationStrategy;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.spi.ThreadPoolFactory;
import org.apache.camel.spi.ThreadPoolProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by vn0y942 on 09/08/17.
 */
@Component
public class CommercialStructureRouteBuilder extends RouteBuilder {

    public static final String ROUTE_LOAD_CSV = "direct:loadCsFile";

    private CommercialStructureProcessor commercialStructureProcessor;

    @Autowired
    private CommercialStructureAssociationRepository repository;

    @Autowired
    private CommercialStructureRepository commercialStructureRepository;

    @Autowired
    private PartnerService partnerService;

    public CommercialStructureRouteBuilder(CamelContext context, CommercialStructureProcessor commercialStructureProcessor) {
        super(context);
        this.commercialStructureProcessor = commercialStructureProcessor;
    }

    @Override
    public void configure() throws EntityNotFoundException {

        ThreadPoolProfile poolProfile = new ThreadPoolProfile("masterPoolProfile");
        poolProfile.setMaxPoolSize(1);
        poolProfile.setMaxQueueSize(1);
        poolProfile.setPoolSize(1);
        poolProfile.setKeepAliveTime(1L);
        poolProfile.setTimeUnit(TimeUnit.MINUTES);

        ThreadPoolFactory poolFactory = new DefaultThreadPoolFactory();
        poolFactory.newThreadPool(poolProfile, Executors.defaultThreadFactory());

        getContext().getExecutorServiceManager().setThreadPoolFactory(poolFactory);
        getContext().getExecutorServiceManager().setDefaultThreadPoolProfile(poolProfile);

        final DataFormat bindy = new BindyCsvDataFormat(CommercialStructureBindy.class);
        from(ROUTE_LOAD_CSV)
                .process(exchange -> {
                    String archiveName = exchange.getIn().getHeader("archiveName", String.class);
                    String partnerSlug = exchange.getIn().getHeader("partnerSlug", String.class);
                    PartnerEntity partner = partnerService.findBySlug(partnerSlug);

                    CommercialStructureEntity savedCommercialStructure = commercialStructureRepository.saveAndFlush(CommercialStructureEntity.builder()
                            .slug(SlugParserUtil.toSlug(archiveName))
                            .partner(partner)
                            .archiveName(archiveName)
//                            .associationEntityList(((List<CommercialStructureBindy>)exchange.getIn().getBody(List.class)).stream().map(c -> CommercialStructureAssociationEntity.builder()
//                                    .partnerTaxonomy(c.getPartnerTaxonomy())
//                                    .structurePartnerId(c.getStructurePartnerId())
//                                    .walmartTaxonomy(c.getWalmartTaxonomy())
//                                    .build()).collect(Collectors.toList()))
                            .build());

                    exchange.getOut().setHeader("commercialStructure", savedCommercialStructure);
                    exchange.getOut().setBody(exchange.getIn().getBody());
                })
                .unmarshal(bindy)
                .split(body())
                .parallelProcessing()
                //.to("direct:teste123")
                .process(exchange -> {
                    getContext();
                    CommercialStructureBindy c = exchange.getIn().getBody(CommercialStructureBindy.class);
                    System.out.println("Id: " + Thread.currentThread().getId() +  " Name: \"" + Thread.currentThread().getName() + "\" Object: \"" + c.getStructurePartnerId() + "\"");
                    repository.save(CommercialStructureAssociationEntity.builder()
                            .structurePartnerId(c.getStructurePartnerId())
                            .walmartTaxonomy(c.getWalmartTaxonomy())
                            .partnerTaxonomy(c.getPartnerTaxonomy())
                            .commercialStructure(exchange.getIn().getHeader("commercialStructure", CommercialStructureEntity.class))
                            .build());

                    exchange.getOut().setBody(exchange.getIn().getBody());
                });
//                .log("Success");

//        from("direct:teste123")


    }

}

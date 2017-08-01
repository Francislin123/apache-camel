package com.walmart.feeds.api.resources.partner;

import com.walmart.feeds.api.core.exceptions.NotFoundException;
import com.walmart.feeds.api.core.service.partner.PartnerService;
import com.walmart.feeds.api.core.service.partner.model.PartnerTO;
import com.walmart.feeds.api.resources.partner.request.PartnerRequest;
import com.walmart.feeds.api.resources.partner.request.PartnerUpdateRequest;
import com.walmart.feeds.api.resources.partner.response.PartnerResponseList;
import com.walmart.feeds.api.resources.partner.response.PartnerResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api
@RestController
@RequestMapping("/v1/partners")
public class PartnerController {

    private Logger logger = LoggerFactory.getLogger(PartnerController.class);

    @Autowired
    private PartnerService service;

	@Autowired
	private ServletContext context;

	@ApiOperation(value = "Create new partner",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful new partner", response = PartnerRequest.class),
            @ApiResponse(code = 409, message = "Partner already exists"),
            @ApiResponse(code = 500, message = "Unhandled exception")})
    @RequestMapping(method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> createPartner(@RequestBody @Valid PartnerRequest partner){

        service.savePartner(buildPartnerTO(partner));
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @ApiOperation(value = "Method to find the partner by your reference",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Partner found", response = PartnerResponse.class),
            @ApiResponse(code = 404, message = "Partner not found")})
	@RequestMapping(value = "/{reference}",
            method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity fetchPartnerByReference(@PathVariable("reference") String reference) throws NotFoundException {

        PartnerResponse partnerRequest = buildPartnerResponse(service.findByReference(reference));
        return new ResponseEntity(partnerRequest, HttpStatus.OK);

    }

    @ApiOperation(value = " Method to change partner fields ",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Partner was updated"),
            @ApiResponse(code = 404, message = "Partner does not exists"),
            @ApiResponse(code = 500, message = "Unhandled error")})
	@RequestMapping(value = "/{reference}",
            method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity updatePartner(@PathVariable("reference") String reference, @Valid @RequestBody PartnerUpdateRequest partnerRequest)
            throws NotFoundException {
		logger.info("Updating partner referenced by {}", reference);

        ModelMapper mapper = new ModelMapper();
        PartnerTO partnerTO = mapper.map(partnerRequest, PartnerTO.class);
        partnerTO.setReference(reference);

        service.updatePartner(partnerTO);

        return ResponseEntity.ok().build();

	}

	@ApiOperation(value = "Method to modify partner status", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Partner status changed"),
            @ApiResponse(code = 500, message = "Failed to change partner status")})
    @RequestMapping(value = "/{reference}",
            method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> changePartnerStatus(@PathVariable("reference") String reference,
                                                 @RequestParam("status") int status) {

        boolean newStatus = status == 1;
        service.changePartnerStatus(reference, newStatus);
        return ResponseEntity.noContent().build();

    }

    @ApiOperation(value = " Method to find the active partners ", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Partners found successfully", response = PartnerResponseList.class),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @RequestMapping(value = "/actives", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity fetchActivePartners() {

        List<PartnerTO> activePartners = service.findActivePartners();
        return ResponseEntity.ok(buildPartnerListResponse(activePartners));

    }

    @ApiOperation(value = "Partner Listing Method",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all partners", response = PartnerResponseList.class),
            @ApiResponse(code = 500, message = "Unhandled exception")})
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity fetchAllPartners() {

        List<PartnerTO> allPartners = service.findAllPartners();
        return ResponseEntity.ok(buildPartnerListResponse(allPartners));

    }

    @ApiOperation(value = " Method for fetching partners using query text ",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Search found partners", response = PartnerResponseList.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Empty result")})
    @RequestMapping(value = "search", method = RequestMethod.GET)
    public ResponseEntity searchPartners(@RequestParam("q") String query) {

        logger.info("Searching partners using query text = {}", query);
        List<PartnerTO> partnerResponses = this.service.searchPartners(query);
        if (partnerResponses.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(buildPartnerListResponse(partnerResponses));

    }

    private PartnerTO buildPartnerTO(PartnerRequest request) {
        PartnerTO partnerTO = new PartnerTO();
        ModelMapper mapper = new ModelMapper();
        mapper.map(request, partnerTO);
        return partnerTO;
    }

    private PartnerResponse buildPartnerResponse(PartnerTO partnerTO) {
        PartnerResponse response = new PartnerResponse();
        ModelMapper mapper = new ModelMapper();
        mapper.map(partnerTO, response);
        return response;
    }

    private PartnerResponseList buildPartnerListResponse(Collection<PartnerTO> partnersTO) {
        List<PartnerResponse> responses =
                partnersTO.stream().map((partnerTO) -> buildPartnerResponse(partnerTO)).collect(Collectors.toList());
        PartnerResponseList listResponse = new PartnerResponseList(responses);
        return listResponse;
    }
}

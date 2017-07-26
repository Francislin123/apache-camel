package com.walmart.feeds.api.resources.partner;

import com.walmart.feeds.api.core.service.partner.PartnerService;
import com.walmart.feeds.api.resources.partner.request.PartnerRequest;
import com.walmart.feeds.api.resources.partner.response.PartnerResponse;
import com.walmart.feeds.api.resources.response.ErrorResponse;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.validation.Valid;
import java.util.List;

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

	@ApiOperation(value = " Create new partner ",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = " Successful new partner ", response = PartnerRequest.class),
            @ApiResponse(code = 409, message = " Partner already exists "),
            @ApiResponse(code = 500, message = " Internal server error ")})
    @RequestMapping(method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> createPartner(@RequestBody @Valid PartnerRequest partner){
        try {

            service.savePartner(partner);

            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (DataIntegrityViolationException e) {
            logger.error("Cannot save the partner " + partner.getName(), e);
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(partner.getName() + " already exists");
        } catch (Exception e) {
            logger.error("Failed to save partner " + partner.getName(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save partner: " + e.getMessage());
        }
    }

    @ApiOperation(value = " Method to find the partner by your reference ",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = " Partner found successfully ", response = PartnerResponse.class),
            @ApiResponse(code = 404, message = " Partner not found ")})
	@RequestMapping(value = "/{reference}",
            method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity fetchPartnerByReference(@PathVariable("reference") String reference) {
        try {

            PartnerResponse partnerRequest = service.findByReference(reference);
            return new ResponseEntity<PartnerResponse>(partnerRequest, HttpStatus.OK);

        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(HttpStatus.NOT_FOUND.toString(),
                    "Partner " + reference + " not found!"));
        }
    }

    @ApiOperation(value = " Method to change partner fields ",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = " Partner change successfully ", response = PartnerRequest.class),
            @ApiResponse(code = 404, message = " Partner not change "),
            @ApiResponse(code = 500, message = " Internal Server Error ")})
	@RequestMapping(value = "/{reference}",
            method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> updatePartner(@PathVariable("reference") String reference,
                                           @RequestBody PartnerRequest partnerRequest) {
		logger.info(" Updating partners with reference {} ", reference);

		partnerRequest.setReference(reference);

        try {

            service.updatePartner(partnerRequest);
            return ResponseEntity.ok().build();

        } catch (NotFoundException e) {
			logger.error("Partner referenced by '{}' not found.", reference);

			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(HttpStatus.NOT_FOUND.toString(),
                            "Partner " + reference + " not found!"));
        } catch (Exception e) {
            logger.error("Failed to update the partner {reference}", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update the partner: " + e.getMessage());
        }

	}

	@ApiOperation(value = " Method to modify partner status  ", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = ""),
            @ApiResponse(code = 500, message = " Failed to change partner status ")})
    @RequestMapping(value = "/{reference}/{status}",
            method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> changePartnerStatus(@PathVariable("reference") String reference,
                                                 @PathVariable("status") String status) {

        try {
            boolean newStatus = "1".equals(status);
            service.setPartnerStatus(reference, newStatus);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Failed to change partner status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to change partner status");
        }

    }

    @ApiOperation(value = " Method for finding active partners ", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = " Partners found successfully ", response = PartnerResponse.class),
            @ApiResponse(code = 500, message = " Internal Server Error ")})
    @RequestMapping(value = "/actives", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<PartnerResponse>> fetchPartnerActives() {
        try {
            return ResponseEntity.ok(service.findActivePartners());
        } catch (Exception e) {
            logger.error("Failed to get all active partners!", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<PartnerResponse>> fetchAllPartners() {
        try {
            return ResponseEntity.ok(service.findAllPartners());
        } catch (Exception e) {
            logger.error("Failed to get all active partners!", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

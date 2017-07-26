package com.walmart.feeds.api.resources.partner;

import java.util.List;

import javax.servlet.ServletContext;
import javax.validation.Valid;

import com.walmart.feeds.api.core.repository.partner.model.Partner;
import com.walmart.feeds.api.resources.partner.response.PartnerResponse;
import com.walmart.feeds.api.resources.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.walmart.feeds.api.core.service.partner.PartnerService;
import com.walmart.feeds.api.resources.partner.request.PartnerRequest;

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

	@ApiOperation(value = "Create new partner",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiResponses(@ApiResponse(code=201,message=" Successful new partner ",response = PartnerRequest.class))
    @RequestMapping(method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> createPartner(@RequestBody @Valid PartnerRequest partner){
        try {

            service.savePartner(partner);

            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (DataIntegrityViolationException e) {
            logger.error("Cannot save the partner " + partner.getName(), e);
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(partner.getName() + " already exists");
        } catch (DataAccessException e) {
            logger.error("Cannot save the partner " + partner.getName(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Some error occurred when try save the partner: " + e.getMessage());
        }
    }

    // Method to find the partner by your reference
	@RequestMapping(value = "/{reference}",
            method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity getPartner(@PathVariable("reference") String reference) {
		PartnerResponse partnerRequest = service.findByReference(reference);
		if (partnerRequest == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.toString(),
                    "Partner " + reference + " not found!"));
		}
		return new ResponseEntity<PartnerResponse>(partnerRequest, HttpStatus.OK);
	}
	
	// Method to change partner fields
	@RequestMapping(value = "/{reference}",
            method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> updatePartner(@PathVariable("reference") String reference, @RequestBody PartnerRequest partnerRequest) {
		logger.info("Updating partners with reference {}", reference);

		partnerRequest.setReference(reference);

        boolean result = false;
        try {
            result = service.updatePartner(partnerRequest);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("There was an error while trying to update the partner: " + e.getMessage());
        }

        if (result) {
			return ResponseEntity.ok().build();
		} else {
			logger.error("Partner referenced by '{}' not found.", reference);
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "Partner " + reference + " not found!"));
		}

	}

    @RequestMapping(value = "/{reference}/{status}",
            method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> changePartnerStatus(@PathVariable("reference") String reference,
                                                 @PathVariable("status") String status) {

        try {
            boolean newStatus = "1".equals(status);

            boolean result = service.setPartnerStatus(reference, newStatus);

            if (!result) {
                String message = String.format("Partner referenced by %s not found.", reference);
                logger.info(message);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), message));
            }

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Failure on change partner status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Some error occurred on processing partner status change.");
        }

    }

    @RequestMapping(value = "/actives",
            method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Partner>> findPartnerActives() {
        try {
            return ResponseEntity.ok(service.findPartnerActives());
        } catch (Exception e) {
            logger.error("Get all actives partners failed!", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

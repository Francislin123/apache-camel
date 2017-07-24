package com.walmart.feeds.api.resources.partner;

import java.net.URI;

import javax.servlet.ServletContext;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.walmart.feeds.api.resources.response.ErrorResponse;

@RestController
@RequestMapping("/v1/partners")
public class PartnerController {

    private Logger logger = LoggerFactory.getLogger(PartnerController.class);

    @Autowired
    private PartnerService service;

	@Autowired
	private ServletContext context;

    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity createPartner(@RequestBody @Valid PartnerRequest partner) {
        try {

            service.savePartner(partner);

            URI location = URI.create(context.getContextPath() +
                    "/partners/" + partner.getReference());
            return ResponseEntity.created(location).build();

        } catch (DataIntegrityViolationException e) {
            logger.error("Cannot save the partner " + partner.getName(), e);
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(partner.getName() + " already exists");
        }
    }

    // Method to find the partner by your reference
	@RequestMapping(value = "/{reference}", method = RequestMethod.GET)
	public ResponseEntity<?> getPartner(@PathVariable("reference") String reference) {
		PartnerRequest partnerRequest = service.findByReference(reference);
		if (partnerRequest == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "Partner " + reference + " not found!"));
		}
		return new ResponseEntity<PartnerRequest>(partnerRequest, HttpStatus.OK);
	}
	
	// Method to change partner fields
	@RequestMapping(value = "/{reference}", method = RequestMethod.PATCH, consumes = "application/json")
	public ResponseEntity<?> updatePartner(@PathVariable("reference") String reference, @RequestBody PartnerRequest partnerRequest) {
		logger.info("Updating partners with reference {}", reference);
		
		partnerRequest.setReference(reference);
		boolean result = service.updatePartner(partnerRequest);

		if (result) {
			return ResponseEntity.ok().build(); 
		} else {
			logger.error("Partner with reference {} not found.", reference);
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "Partner " + reference + " not found!"));
		}
    
	}

    @RequestMapping(value = "/{reference}/{status}", method = RequestMethod.PATCH)
    public ResponseEntity<?> changePartnerStatus(@PathVariable("reference") String reference,
                                                 @PathVariable("status") String status) {

        try {
            boolean newStatus = "1".equals(status);

            service.setPartnerStatus(reference, newStatus);

            // improvement: check which partner was modified
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Failure on change partner status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Some error occurred on processing partner status change.");
        }

    }
}

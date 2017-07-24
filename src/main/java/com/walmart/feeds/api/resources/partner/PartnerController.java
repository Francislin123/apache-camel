package com.walmart.feeds.api.resources.partner;

import com.walmart.feeds.api.core.service.partner.PartnerService;
import com.walmart.feeds.api.resources.partner.request.PartnerRequest;
import com.walmart.feeds.api.resources.response.ErrorResponse;
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
import java.net.URI;

@RestController
@RequestMapping("/partners")
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
	public ResponseEntity<?> getPartnerRequest(@PathVariable("reference") String reference) {
		PartnerRequest partnerRequest = service.findByReference(reference);
		if (partnerRequest == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "Partner " + reference + " not found!"));
		}
		return new ResponseEntity<PartnerRequest>(partnerRequest, HttpStatus.OK);
	}

}

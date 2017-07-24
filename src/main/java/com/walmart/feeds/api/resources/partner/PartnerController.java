package com.walmart.feeds.api.resources.partner;

import java.net.URI;

import javax.servlet.ServletContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/partners")
public class PartnerController {

	@Autowired
	private PartnerService service;

	@Autowired
	private ServletContext context;

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> createPartner(@RequestBody @Valid PartnerRequest partner) {
		service.savePartner(partner);
		return ResponseEntity.created(URI.create(context.getContextPath())).build();
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

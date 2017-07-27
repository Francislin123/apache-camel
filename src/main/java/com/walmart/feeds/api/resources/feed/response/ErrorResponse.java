package com.walmart.feeds.api.resources.feed.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Created by r0i001q on 19/07/17.
 */
@Data
public class ErrorResponse {

    private String code;
    private String description;
	private List<FieldValidation> fieldValidations;

	public ErrorResponse() {
		this(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Unhandled error");
	}
	
    public ErrorResponse(String code, String description) {
		super();
		this.code = code;
		this.description = description;
	}

	public ErrorResponse(String code, String description, List<FieldValidation> validations) {
		super();
		this.code = code;
		this.description = description;
		this.fieldValidations = validations;
	}

}

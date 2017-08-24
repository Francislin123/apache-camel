package com.walmart.feeds.api.resources.feed.response;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Created by r0i001q on 19/07/17.
 */
@Getter
public class ErrorResponse {

    private String code;
    private String description;
	private List<FieldValidation> fieldValidations;

    @Tolerate
    public ErrorResponse() {
        new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Unhandled Exception", null);
    }

    @Builder
    private ErrorResponse(String code, String description, List<FieldValidation> validations) {
        super();
		this.code = code;
		this.description = description;
		this.fieldValidations = validations;
	}

}

package com.walmart.feeds.api.resources.feed.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Created by r0i001q on 19/07/17.
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private String code;
    private String description;
	private List<String> invalidList;
	private List<FieldValidation> fieldValidations;

    @Tolerate
    public ErrorResponse() {
        new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Unhandled Exception", invalidList, null);
    }

    @Builder
    private ErrorResponse(String code, String description, List<String> invalidList, List<FieldValidation> validations) {
        super();
		this.code = code;
		this.description = description;
        this.invalidList = invalidList;
        this.fieldValidations = validations;
	}

}

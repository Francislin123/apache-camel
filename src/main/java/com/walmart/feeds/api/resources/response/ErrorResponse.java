package com.walmart.feeds.api.resources.response;

import lombok.Data;

/**
 * Created by r0i001q on 19/07/17.
 */
@Data
public class ErrorResponse {

    private String code;
    private String description;

	public ErrorResponse() {
		this("400", "");
	}
	
    public ErrorResponse(String code, String description) {
		super();
		this.code = code;
		this.description = description;
	}

}

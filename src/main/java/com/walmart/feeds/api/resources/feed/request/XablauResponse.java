package com.walmart.feeds.api.resources.feed.request;

import com.walmart.feeds.api.resources.feed.response.ErrorResponse;
import lombok.Data;

/**
 * Created by r0i001q on 19/07/17.
 */
@Data
public class XablauResponse {

    private String status;
    private ErrorResponse error;


}

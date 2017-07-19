package com.walmart.feeds.api.resources;

import com.walmart.feeds.api.resources.request.XablauRequest;
import com.walmart.feeds.api.resources.request.XablauResponse;
import com.walmart.feeds.api.resources.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by r0i001q on 19/07/17.
 */
@RestController
@RequestMapping("/xablau")
public class XablauController {

    @RequestMapping(method = RequestMethod.GET, consumes = "application/json")
    public ResponseEntity<XablauResponse> fetchAllXablau() {

        XablauResponse xablauResponse = new XablauResponse();

        xablauResponse.setStatus("sucesso");
        ErrorResponse error = new ErrorResponse();
        error.setCode("00");
        error.setDescription("alsjdlaskd");
        xablauResponse.setError(error);

        return ResponseEntity.ok(xablauResponse);
    }

    @RequestMapping(value = "/{xablauId}", method = RequestMethod.GET, consumes = "application/json")
    public ResponseEntity<XablauResponse> fetchXablau(@PathVariable("xablauId") String xablauId) {

        XablauResponse xablauResponse = new XablauResponse();

        xablauResponse.setStatus(String.format("Xablau %s recuperado com sucesso", xablauId));

        return ResponseEntity.ok(xablauResponse);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<XablauResponse> createXablau(@RequestBody XablauRequest request) throws URISyntaxException {

        XablauResponse xablauResponse = new XablauResponse();

        xablauResponse.setStatus(String.format("Xablau %s criado com sucesso.", request.getId()));

        return ResponseEntity.created(new URI(String.format("/xablau/%s", request.getId()))).body(xablauResponse);
    }


}

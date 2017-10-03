package com.walmart.feeds.api.resources.generator;

import com.walmart.feeds.api.core.repository.generator.GenerationHistoryRepository;
import com.walmart.feeds.api.core.repository.generator.model.GenerationHistoryEntity;
import com.walmart.feeds.api.resources.generator.request.GenerationHistoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(GenerationHistoryController.GENERATION_HISTORY)
public class GenerationHistoryController {

    public static final String GENERATION_HISTORY = "/v1/generation-histories";

    @Autowired
    private GenerationHistoryRepository historyRepository;

    // No Swagger Documentation. It is for the exclusive use of feed generator
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity save(@RequestBody @Valid GenerationHistoryRequest generationHistory) {

        GenerationHistoryEntity historyEntity = GenerationHistoryEntity.builder()
                .fileName(generationHistory.getFileName())
                .feedSlug(generationHistory.getFeedSlug())
                .partnerSlug(generationHistory.getPartnerSlug())
                .totalSkus(generationHistory.getTotalSkus())
                .generationDate(generationHistory.getGenerationDate())
                .build();

        historyRepository.save(historyEntity);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}

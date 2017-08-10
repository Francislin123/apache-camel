package com.walmart.feeds.api.resources.commercialstructure.service;

import com.walmart.feeds.api.core.exceptions.NotFoundException;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by vn0y942 on 08/08/17.
 */
public interface CommercialStructureService {

    void loadCommercialStructure(MultipartFile file, String partnerSlug, String feedSlug) throws NotFoundException;

    List<CommercialStructureEntity> readCVSFile(MockMultipartFile commercialStructureFile, String partnerSlug, String feedSlug) throws IOException;
}

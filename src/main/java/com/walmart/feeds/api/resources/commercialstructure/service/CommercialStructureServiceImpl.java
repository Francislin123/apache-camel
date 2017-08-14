package com.walmart.feeds.api.resources.commercialstructure.service;

import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.repository.commercialstructure.CommercialStructureRepository;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

/**
 * Created by vn0y942 on 08/08/17.
 */
@Service
public class CommercialStructureServiceImpl implements CommercialStructureService{

    @Autowired
    CommercialStructureRepository commercialStructureRepository;

    @Override
    public void loadCommercialStructure(CommercialStructureEntity commercialStructureEntity) throws EntityNotFoundException {
        
    }


}

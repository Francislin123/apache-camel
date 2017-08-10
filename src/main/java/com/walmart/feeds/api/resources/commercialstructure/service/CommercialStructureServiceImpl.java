package com.walmart.feeds.api.resources.commercialstructure.service;

import com.walmart.feeds.api.core.exceptions.NotFoundException;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureEntity;
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
    @Override
    public void loadCommercialStructure(MultipartFile file, String partnerSlug, String feedSlug) throws NotFoundException {
        if (file.isEmpty()){
            throw new NotFoundException("File is Empty");
        }else{

        }
    }

    @Override
    public List<CommercialStructureEntity> readCVSFile(MockMultipartFile commercialStructureFile, String partnerSlug, String feedSlug) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(commercialStructureFile.getInputStream()));
        String line = "";
        int lineNumber = 0;
        while((line = br.readLine()) != null){
            String[] structure = line.split(";");

        }
        return null;
    }
}

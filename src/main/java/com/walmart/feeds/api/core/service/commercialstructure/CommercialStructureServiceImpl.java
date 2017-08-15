package com.walmart.feeds.api.core.service.commercialstructure;

import com.walmart.feeds.api.core.repository.commercialstructure.CommercialStructureRepository;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by vn0y942 on 15/08/17.
 */
@Service
public class CommercialStructureServiceImpl implements CommercialStructureService{

    @Autowired
    private CommercialStructureRepository commercialStructureRepository;

    @Override
    @Transactional
    public void loadFile(CommercialStructureEntity commercialStructureEntity) {
        commercialStructureRepository.findBySlug(commercialStructureEntity.getSlug()).ifPresent(
                (commercialStructure) -> commercialStructureRepository.delete(((CommercialStructureEntity) commercialStructure))
        );


    }

}

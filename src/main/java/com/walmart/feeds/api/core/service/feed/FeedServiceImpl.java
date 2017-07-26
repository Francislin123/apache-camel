package com.walmart.feeds.api.core.service.feed;

import com.walmart.feeds.api.core.repository.feed.FeedRepository;
import com.walmart.feeds.api.core.repository.feed.model.FeedEntity;
import com.walmart.feeds.api.core.repository.feed.model.UTM;
import com.walmart.feeds.api.core.service.feed.model.FeedTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by vn0y942 on 21/07/17.
 */
@Service
public class FeedServiceImpl implements FeedService {

    @Autowired
    private FeedRepository feedRepository;

//    private PartnerRepository partnerRepository;


    @Override
    public void createFeed(FeedTO feedTO) {

        // TODO[r0i001q]: validate if exists partner by reference

//        partnerRepository.findByReference(feedTO.getPartnerReference()).orElseThrow(RuntimeException::new);

        FeedEntity feedEntity = new FeedEntity();

        feedEntity.setPartnerId(feedTO.getPartnerReference());

        feedEntity.setUtms(feedTO.getUtms().stream().map(u -> {
            UTM utmRepo = new UTM();
            utmRepo.setValue(u.getValue());
            utmRepo.setType(u.getType());
            utmRepo.setFeed(feedEntity);
            return utmRepo;
        }).collect(Collectors.toList()));

        feedEntity.setType(feedTO.getType());
        feedEntity.setReference(feedTO.getReference());
        feedEntity.setType(feedTO.getType());
        feedEntity.setNotificationUrl(feedTO.getNotificationData().getUrl());
        feedEntity.setNotificationMethod(feedTO.getNotificationData().getMethod());
        feedEntity.setNotificationFormat(feedTO.getNotificationData().getFormat());
        feedEntity.setName(feedTO.getName());

        feedRepository.save(feedEntity);

    }
    @Override
    public List<FeedTO> fetchActive(FeedTO feedTo){
        List<FeedEntity> feedEntities  = feedRepository.findByActive(feedTo.isActive()).orElseThrow(NoResultException::new);
        ModelMapper mapper = new ModelMapper();
        return feedEntities.stream().map(feedEntity -> mapper.map(feedEntity, FeedTO.class)).collect(Collectors.toList());
    }
    @Override
    public List<FeedTO> fetchByPartner(FeedTO feedTO){
        List<FeedEntity> feedEntities = feedRepository.findByPartnerId(feedTO.getPartnerReference()).orElseThrow(NoResultException::new);
        ModelMapper mapper = new ModelMapper();
        return feedEntities.stream().map(feedEntity -> mapper.map(feedEntity, FeedTO.class)).collect(Collectors.toList());
    }

}

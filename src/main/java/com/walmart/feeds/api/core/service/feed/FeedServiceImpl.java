package com.walmart.feeds.api.core.service.feed;

import com.walmart.feeds.api.core.repository.feed.FeedRepository;
import com.walmart.feeds.api.core.repository.feed.model.FeedEntity;
import com.walmart.feeds.api.core.repository.feed.model.UTM;
import com.walmart.feeds.api.core.service.feed.model.FeedTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;
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
        //TODO THIAGO LIMA: Retirar Mock
//        List<FeedEntity> feedEntities = feedRepository.findByActive(feedTo.isActive());
        List<FeedEntity> feedEntities = this.mockFeed();
        ModelMapper mapper = new ModelMapper();
        return feedEntities.stream().map(feedEntity -> mapper.map(feedEntity, FeedTO.class)).collect(Collectors.toList());
    }
    @Override
    public List<FeedTO> fetchByPartner(FeedTO feedTO){
        //TODO THIAGO LIMA: Retirar Mock
//        List<FeedEntity> feedEntities = feedRepository.findByPartnerId(feedTO.getPartnerId());
        List<FeedEntity> feedEntities = this.mockFeed();
        ModelMapper mapper = new ModelMapper();
        return feedEntities.stream().map(feedEntity -> mapper.map(feedEntity, FeedTO.class)).collect(Collectors.toList());
    }
    @Override
    public List<FeedTO> fetch(){
        //TODO THIAGO LIMA: Retirar Mock
//        List<FeedEntity> feedEntities = feedRepository.findAll();
        List<FeedEntity> feedEntities = this.mockFeed();
        ModelMapper mapper = new ModelMapper();
        return feedEntities.stream().map(feedEntity -> mapper.map(feedEntity, FeedTO.class)).collect(Collectors.toList());

    }


    private List<FeedEntity> mockFeed(){
        //TODO THIAGO LIMA: Retirar Mock
        LocalDateTime now = LocalDateTime.now();
        List<FeedEntity> lista = new ArrayList<>();
        FeedEntity feed = new FeedEntity();
        feed.setActive(true);
        feed.setCreationDate(now);
        feed.setName("Teste");

        FeedEntity feed2 = new FeedEntity();
        feed2.setActive(true);
        feed2.setCreationDate(now);
        feed2.setName("Teste 2");

        lista.add(feed);
        lista.add(feed2);
        return lista;
    }

}

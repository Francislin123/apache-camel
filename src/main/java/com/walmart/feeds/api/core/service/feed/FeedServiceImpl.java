package com.walmart.feeds.api.core.service.feed;

import com.walmart.feeds.api.core.repository.feed.FeedRepository;
import com.walmart.feeds.api.core.repository.feed.model.FeedEntity;
import com.walmart.feeds.api.core.repository.partner.PartnerRepository;
import com.walmart.feeds.api.core.repository.partner.model.Partner;
import com.walmart.feeds.api.core.service.feed.model.FeedTO;
import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by vn0y942 on 21/07/17.
 */
@Service
public class FeedServiceImpl implements FeedService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Override
    public void createFeed(FeedTO feedTO) throws NotFoundException {

        Partner partner = partnerRepository.findByReference(feedTO.getPartnerReference()).orElseThrow(()  -> new NotFoundException(String.format("Partner not found for reference %s", feedTO.getPartnerReference())));

        ModelMapper modelMapper = new ModelMapper();

        FeedEntity feedEntity = new FeedEntity();
        modelMapper.map(feedTO, feedEntity);
        feedEntity.getUtms().stream().forEach(u -> u.setFeed(feedEntity));
        feedEntity.setPartner(partner);

        FeedEntity savedFeed = feedRepository.save(feedEntity);

        logger.info("feed={} message=saved_succesfully", savedFeed);

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
    public void removeFeed(String reference) throws NotFoundException {

        FeedEntity feedEntity = feedRepository.findByReference(reference).orElseThrow(()->new NotFoundException("Feed not Found"));//busca no banco a partir do reference

        feedEntity.setUpdateDate(LocalDateTime.now());
        feedRepository.changeFeedStatus(feedEntity, false);

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

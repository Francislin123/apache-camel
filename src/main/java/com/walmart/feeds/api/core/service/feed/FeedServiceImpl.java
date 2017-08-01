package com.walmart.feeds.api.core.service.feed;

import com.walmart.feeds.api.core.converter.FeedConverter;
import com.walmart.feeds.api.core.exceptions.NotFoundException;
import com.walmart.feeds.api.core.repository.feed.FeedRepository;
import com.walmart.feeds.api.core.repository.feed.model.Feed;
import com.walmart.feeds.api.core.repository.partner.PartnerRepository;
import com.walmart.feeds.api.core.repository.partner.model.Partner;
import com.walmart.feeds.api.core.service.feed.model.FeedTO;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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

        Partner partner = partnerRepository.findByReference(feedTO.getPartner().getReference()).orElseThrow(()  -> new NotFoundException(String.format("Partner not found for reference %s", feedTO.getPartner().getReference())));

        ModelMapper modelMapper = new ModelMapper();

        Feed feedEntity = new Feed();
        modelMapper.map(feedTO, feedEntity);
        feedEntity.setPartner(partner);
        feedEntity.setCreationDate(LocalDateTime.now());
        Feed savedFeed = feedRepository.save(feedEntity);

        logger.info("feed={} message=saved_successfully", savedFeed);

    }

    @Override
    public List<FeedTO> fetchByPartner(String partnerReference, Boolean active) throws NotFoundException {
        Partner partner = partnerRepository.findByReference(partnerReference).orElseThrow(()  -> new NotFoundException(String.format("Partner not found for reference %s", partnerReference)));
        List<Feed> feedEntities = null;
        if(active != null){
            feedEntities = feedRepository.findByActiveAndPartner(active, partner).orElseThrow(() -> new NotFoundException("Feed not found"));
        }else{
            feedEntities = feedRepository.findByPartner(partner).orElseThrow(() -> new NotFoundException("Feed not found"));
        }
        return feedEntities.stream().map(feedEntity -> FeedConverter.convert(feedEntity)).collect(Collectors.toList());
    }

    @Override
    public void changeFeedStatus(String reference, Boolean active) throws NotFoundException {
        Feed feedEntity = feedRepository.findByReference(reference).orElseThrow(() -> new NotFoundException("Feed not Found"));//busca no banco a partir do reference
        feedEntity.setUpdateDate(LocalDateTime.now());
        feedRepository.changeFeedStatus(feedEntity, active);
    }

    @Override
    public void updateFeed(FeedTO feedTO) throws DataIntegrityViolationException, NotFoundException {
        Partner partner = partnerRepository.findByReference(feedTO.getPartner().getReference()).orElseThrow(() -> new NotFoundException("Partner not Found"));

        Feed persistedFeed = feedRepository.findByReference(feedTO.getReference()).orElseThrow(() -> new NotFoundException("Feed not Found"));
        Feed entity = FeedConverter.convert(feedTO);
        entity.setId(persistedFeed.getId());
        entity.setUpdateDate(LocalDateTime.now());
        entity.setCreationDate(persistedFeed.getCreationDate());
        entity.setPartner(partner);
        feedRepository.save(entity);
        logger.info("feed={} message=update_successfully", entity);
    }
}

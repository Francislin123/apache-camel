package com.walmart.feeds.api.core.service.feed;

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

        Partner partner = partnerRepository.findByReference(feedTO.getPartnerReference()).orElseThrow(()  -> new NotFoundException(String.format("Partner not found for reference %s", feedTO.getPartnerReference())));

        ModelMapper modelMapper = new ModelMapper();

        Feed feedEntity = new Feed();
        modelMapper.map(feedTO, feedEntity);
        feedEntity.getUtms().stream().forEach(u -> u.setFeed(feedEntity));
        feedEntity.setPartner(partner);

        Feed savedFeed = feedRepository.save(feedEntity);

        logger.info("feed={} message=saved_successfully", savedFeed);

    }

    @Override
    public List<FeedTO> fetchByActiveAndByPartner(FeedTO feedTO) throws NotFoundException {
        Partner partner = partnerRepository.findByReference(feedTO.getPartnerReference()).orElseThrow(()  -> new NotFoundException(String.format("Partner not found for reference %s", feedTO.getPartnerReference())));
        List<Feed> feedEntities = feedRepository.findByActiveAndPartner(feedTO.isActive(), partner).orElseThrow(() -> new NotFoundException("Feed not found"));
        ModelMapper mapper = new ModelMapper();
        return feedEntities.stream().map(feedEntity -> mapper.map(feedEntity, FeedTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<FeedTO> fetchByPartner(FeedTO feedTO) throws NotFoundException {
        Partner partner = partnerRepository.findByReference(feedTO.getPartnerReference()).orElseThrow(()  -> new NotFoundException(String.format("Partner not found for reference %s", feedTO.getPartnerReference())));
        List<Feed> feedEntities = feedRepository.findByPartner(partner).orElseThrow(() -> new NotFoundException("Feed not found"));
        ModelMapper mapper = new ModelMapper();
        return feedEntities.stream().map(feedEntity -> mapper.map(feedEntity, FeedTO.class)).collect(Collectors.toList());
    }

    @Override
    public void updateFeed(FeedTO feedTO) throws DataIntegrityViolationException {
        ModelMapper mapper = new ModelMapper();
        Feed entity = mapper.map(feedTO, Feed.class);
        feedRepository.save(entity);
        logger.info("feed={} message=update_successfully", entity);
    }
}

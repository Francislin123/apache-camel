package com.walmart.feeds.api.core.service.fields;

import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.exceptions.InconsistentEntityException;
import com.walmart.feeds.api.core.exceptions.UserException;
import com.walmart.feeds.api.core.repository.fields.FieldsMappingHistoryRepository;
import com.walmart.feeds.api.core.repository.fields.FieldsMappingRepository;
import com.walmart.feeds.api.core.repository.fields.model.FieldsMappingEntity;
import com.walmart.feeds.api.core.repository.fields.model.FieldsMappingHistory;
import com.walmart.feeds.api.core.utils.MapperUtil;
import com.walmart.feeds.api.core.utils.SlugParserUtil;
import com.walmart.feeds.api.persistence.ElasticSearchComponent;
import com.walmart.feeds.api.resources.common.response.SimpleError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FieldsMappingServiceImpl implements FieldsMappingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FieldsMappingServiceImpl.class);

    @Autowired
    private FieldsMappingRepository fieldsMappingRepository;

    @Autowired
    private FieldsMappingHistoryRepository historyRepository;

    @Autowired
    private ElasticSearchComponent elasticSearchComponent;

    @Override
    @Transactional
    public void save(FieldsMappingEntity fieldsMappingEntity) {

        if (fieldsMappingEntity == null) {
            throw new InconsistentEntityException("Null fields mapping");
        }

        hasConflict(fieldsMappingEntity.getSlug());

        persistFieldsMapping(fieldsMappingEntity);
    }

    @Override
    @Transactional
    public void update(FieldsMappingEntity fieldsMapping) {

        if (fieldsMapping == null) {
            throw new InconsistentEntityException("Null fields mapping");
        }

        String newSlug = SlugParserUtil.toSlug(fieldsMapping.getName());

        if (!fieldsMapping.getSlug().equals(newSlug)) {
            hasConflict(newSlug);
        }

        FieldsMappingEntity persistedEntity = fieldsMappingRepository.findBySlug(fieldsMapping.getSlug())
                .orElseThrow(() -> new EntityNotFoundException(String.format("FieldsMapping %s not found", fieldsMapping.getSlug())));


        FieldsMappingEntity updatedEntity = FieldsMappingEntity.builder()
                .id(persistedEntity.getId())
                .creationDate(persistedEntity.getCreationDate())
                .name(fieldsMapping.getName())
                .slug(newSlug)
                .mappedFields(fieldsMapping.getMappedFields())
                .build();

        persistFieldsMapping(updatedEntity);

    }

    @Override
    public void delete(String slug) {
        FieldsMappingEntity fieldsMappingDelete = findBySlug(slug);
        this.fieldsMappingRepository.delete(fieldsMappingDelete);
    }

    @Override
    public FieldsMappingEntity findBySlug(String slug) {

        FieldsMappingEntity fieldsMapping = fieldsMappingRepository.findBySlug(slug).orElseThrow(() ->
                new EntityNotFoundException(String.format("FieldsMappging %s not found!", slug)));

        return fieldsMapping;

    }

    @Override
    public List<FieldsMappingEntity> findAll() {
        List<FieldsMappingEntity> fieldsMapping = fieldsMappingRepository.findAll();
        LOGGER.info("Total of fields mapping: {}", fieldsMapping.size());
        return fieldsMapping;
    }

    @Override
    public void hasConflict(String slug) {

        if (fieldsMappingRepository.findBySlug(slug).isPresent()) {
            throw new EntityAlreadyExistsException(String.format("Fields mapping called '%s' already exists", slug));
        }
    }

    private void persistFieldsMapping(FieldsMappingEntity fieldsMapping) {

        List<String> walmartFields = elasticSearchComponent.getWalmartFields();

        List<SimpleError> invalidWalmartFields = fieldsMapping.getMappedFields().stream()
                .filter(field -> !walmartFields.contains(field.getWmField()))
                .map(field -> SimpleError.builder()
                        .message(field.getWmField())
                        .build())
                .collect(Collectors.toList());

        if (!invalidWalmartFields.isEmpty()) {
            throw new UserException("These walmart fields does not exists", invalidWalmartFields);
        }

        FieldsMappingEntity managedEntity = fieldsMappingRepository.saveAndFlush(fieldsMapping);
        LOGGER.info("fieldsMapping={} message=saved_successfully", fieldsMapping);

        FieldsMappingHistory history = buildHistory(managedEntity);

        historyRepository.save(history);
        LOGGER.info("fieldsMappingHistory={} message=saved_successfully", history);

    }

    private FieldsMappingHistory buildHistory(FieldsMappingEntity fieldsMapping) {
        return FieldsMappingHistory.builder()
                    .name(fieldsMapping.getName())
                    .slug(fieldsMapping.getSlug())
                    .creationDate(fieldsMapping.getCreationDate())
                    .updateDate(fieldsMapping.getUpdateDate())
                    .user(fieldsMapping.getUser())
                    .mappedFields(MapperUtil.getMapsAsJson(fieldsMapping.getMappedFields()))
                    .build();
    }

}

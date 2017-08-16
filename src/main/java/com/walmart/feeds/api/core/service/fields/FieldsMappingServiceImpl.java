package com.walmart.feeds.api.core.service.fields;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walmart.feeds.api.core.exceptions.*;
import com.walmart.feeds.api.core.repository.fields.FieldsMappingHistoryRepository;
import com.walmart.feeds.api.core.repository.fields.FieldsMappingRepository;
import com.walmart.feeds.api.core.repository.fields.model.FieldsMappingEntity;
import com.walmart.feeds.api.core.repository.fields.model.FieldsMappingHistory;
import com.walmart.feeds.api.core.repository.fields.model.MappedFieldEntity;
import com.walmart.feeds.api.core.utils.SlugParserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FieldsMappingServiceImpl implements FieldsMappingService {

    private Logger logger = LoggerFactory.getLogger(FieldsMappingServiceImpl.class);

    @Autowired
    private FieldsMappingRepository fieldsMappingRepository;

    @Autowired
    private FieldsMappingHistoryRepository historyRepository;

    @Override
    @Transactional
    public void save(FieldsMappingEntity fieldsMappingEntity) throws IllegalArgumentException {

        if (fieldsMappingEntity == null) {
            throw new InconsistentEntityException("null fields mapping");
        }

        if (fieldsMappingRepository.findBySlug(fieldsMappingEntity.getSlug()).isPresent()) {
            throw new EntityAlreadyExistsException(String.format("Fields mapping with slug='%s' already exists", fieldsMappingEntity.getSlug()));
        }

        persistFieldsMapping(fieldsMappingEntity);
    }

    @Override
    @Transactional
    public void update(FieldsMappingEntity fieldsMapping) throws EntityNotFoundException {

        if (fieldsMapping == null) {
            throw new InconsistentEntityException("null fields mapping");
        }

        FieldsMappingEntity persistedEntity = fieldsMappingRepository.findBySlug(fieldsMapping.getSlug())
                .orElseThrow(() -> new EntityNotFoundException(String.format("FieldsMapping %s not found", fieldsMapping.getSlug())));

        FieldsMappingEntity updatedEntity = FieldsMappingEntity.builder()
                .id(persistedEntity.getId())
                .creationDate(persistedEntity.getCreationDate())
                .name(fieldsMapping.getName())
                .slug(SlugParserUtil.toSlug(fieldsMapping.getName()))
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
    public FieldsMappingEntity findBySlug(String slug) throws EntityNotFoundException {

        FieldsMappingEntity fieldsMapping = fieldsMappingRepository.findBySlug(slug).orElseThrow(() ->
                new EntityNotFoundException(String.format("FieldsMappging %s not found!", slug)));

        return fieldsMapping;

    }

    @Override
    public List<FieldsMappingEntity> findAll() {
        List<FieldsMappingEntity> fieldsMapping = fieldsMappingRepository.findAll();
        logger.info("Total of fields mapping: {}", fieldsMapping.size());
        return fieldsMapping;
    }

    private void persistFieldsMapping(FieldsMappingEntity fieldsMapping) {

        fieldsMapping = fieldsMappingRepository.saveAndFlush(fieldsMapping);
        logger.info("fieldsMapping={} message=saved_successfully", fieldsMapping);

        FieldsMappingHistory history = buildHistory(fieldsMapping);

        historyRepository.save(history);
        logger.info("fieldsMappingHistory={} message=saved_successfully", history);

    }

    private FieldsMappingHistory buildHistory(FieldsMappingEntity fieldsMapping) {
        return FieldsMappingHistory.builder()
                    .name(fieldsMapping.getName())
                    .slug(fieldsMapping.getSlug())
                    .creationDate(fieldsMapping.getCreationDate())
                    .updateDate(fieldsMapping.getUpdateDate())
                    .user(fieldsMapping.getUser())
                    .mappedFields(getMappedFieldsAsJson(fieldsMapping.getMappedFields()))
                    .build();
    }

    private String getMappedFieldsAsJson(List<MappedFieldEntity> mappedFields) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.USE_ANNOTATIONS, true);

        try {
            return mapper.writeValueAsString(mappedFields);
        } catch (JsonProcessingException e) {
            throw new SystemException("Error to convert mapped fields to json for history.");
        }

    }
}

package com.walmart.feeds.api.core.service.fields;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
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
import java.util.ArrayList;
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
    public void saveFieldsdMapping(FieldsMappingEntity fieldsMappingEntity) throws IllegalArgumentException {

        if (fieldsMappingRepository.findBySlug(fieldsMappingEntity.getSlug()).isPresent()) {
            throw new EntityAlreadyExistsException(String.format("Fields mapping with slug='%s' already exists", fieldsMappingEntity.getSlug()));
        }

        if (fieldsMappingEntity.getMappedFields().isEmpty()){
            throw new IllegalArgumentException("No mapped fields related with fields mapping " + fieldsMappingEntity.getName());
        }

        persistFieldsMapping(fieldsMappingEntity);
    }

    @Override
    @Transactional
    public void updateFieldsMapping(FieldsMappingEntity fieldsMapping) throws EntityNotFoundException {

        if (fieldsMapping == null) {
            throw new RuntimeException("null mappingentity");
        }

        FieldsMappingEntity persistedEntity = fieldsMappingRepository.findBySlug(fieldsMapping.getSlug())
                .orElseThrow(() -> new EntityNotFoundException(String.format("FieldsMapping %s not found", fieldsMapping.getSlug())));

        FieldsMappingEntity updatedEntity = FieldsMappingEntity.builder()
                .id(persistedEntity.getId())
                .creationDate(persistedEntity.getCreationDate())
                .updateDate(LocalDateTime.now())
                .user(persistedEntity.getUser())
                .name(fieldsMapping.getName())
                .slug(SlugParserUtil.toSlug(fieldsMapping.getName()))
                .mappedFields(fieldsMapping.getMappedFields())
                .build();

        persistFieldsMapping(updatedEntity);

    }

    @Override
    public FieldsMappingEntity findBySlug(String slug) throws EntityNotFoundException {

        FieldsMappingEntity fieldsMapping = fieldsMappingRepository.findBySlug(slug).orElseThrow(() ->
                new EntityNotFoundException(String.format("FieldsMappging %s not found!", slug)));

        return fieldsMapping;

    }

    private void persistFieldsMapping(FieldsMappingEntity updatedEntity) {

        FieldsMappingHistory history = FieldsMappingHistory.builder()
            .name(updatedEntity.getName())
            .slug(updatedEntity.getSlug())
            .mappedFields(getMappedFieldsAsJson(updatedEntity.getMappedFields()))
            .build();


        fieldsMappingRepository.saveAndFlush(updatedEntity);
        logger.info("fieldsMapping={} message=saved_successfully", updatedEntity);


        historyRepository.saveAndFlush(history);
        logger.info("fieldsMappingHistory={} message=saved_successfully", history);

    }

    private String getMappedFieldsAsJson(List<MappedFieldEntity> mappedFields) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.USE_ANNOTATIONS, true);
        try {
            return mapper.writeValueAsString(mappedFields);
        } catch (JsonProcessingException e) {
            logger.error("Error to convert mapped fields to json", e);
        }
        return "";
    }
}

package com.walmart.feeds.api.unit.core.service.fields;

import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.exceptions.UserException;
import com.walmart.feeds.api.core.repository.fields.FieldsMappingHistoryRepository;
import com.walmart.feeds.api.core.repository.fields.FieldsMappingRepository;
import com.walmart.feeds.api.core.repository.fields.model.FieldsMappingEntity;
import com.walmart.feeds.api.core.repository.fields.model.FieldsMappingHistory;
import com.walmart.feeds.api.core.repository.fields.model.MappedFieldEntity;
import com.walmart.feeds.api.core.service.fields.FieldsMappingService;
import com.walmart.feeds.api.core.service.fields.FieldsMappingServiceImpl;
import com.walmart.feeds.api.persistence.ElasticSearchComponent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class FieldsMappingServiceTest {

    @Mock
    private FieldsMappingRepository fmRepository;

    @Mock
    private FieldsMappingHistoryRepository historyRepository;

    @Mock
    private ElasticSearchComponent elasticSearchComponent;

    @InjectMocks
    private FieldsMappingService mappingService = new FieldsMappingServiceImpl();

    @Before
    public void init() {

        Mockito.when(elasticSearchComponent.getWalmartFields())
                .thenReturn(Arrays.asList("name"));

    }

    @Test
    public void testSaveFieldsdMapping() throws Exception {

        FieldsMappingEntity fieldsMapping = createFieldsMapping();

        Mockito.when(fmRepository.findBySlug(anyString()))
                .thenReturn(Optional.empty());
        Mockito.when(fmRepository.saveAndFlush(any(FieldsMappingEntity.class)))
                .thenReturn(createFieldsMapping());

        mappingService.save(createFieldsMapping());

        Mockito.verify(fmRepository).findBySlug(anyString());
        Mockito.verify(fmRepository).saveAndFlush(any(FieldsMappingEntity.class));
        Mockito.verify(historyRepository).save(any(FieldsMappingHistory.class));

    }

    @Test
    public void testSaveFieldsdMappingWhenWalmartFieldNotExist() throws Exception {

        Mockito.when(fmRepository.findBySlug(anyString()))
                .thenReturn(Optional.empty());

        try {

            FieldsMappingEntity fieldsMapping = createFieldsMapping();
            fieldsMapping.getMappedFields()
                    .add(MappedFieldEntity.builder().wmField("price").build());

            mappingService.save(fieldsMapping);

            Assert.fail(UserException.class.getName() + " expected");

        } catch (UserException e) {

            Assert.assertEquals("The walmart fields does not exists: [price]", e.getMessage());
            Mockito.verify(fmRepository).findBySlug(anyString());
            Mockito.verify(fmRepository, Mockito.times(0)).saveAndFlush(any(FieldsMappingEntity.class));
            Mockito.verify(historyRepository, Mockito.times(0)).saveAndFlush(any(FieldsMappingHistory.class));

        }

    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void testSaveFieldsdMappingDuplicatedConstraint() throws Exception {

        Mockito.when(fmRepository.findBySlug(anyString()))
                .thenReturn(Optional.of(createFieldsMapping()));

        mappingService.save(createFieldsMapping());

    }

    @Test
    public void testUpdateFieldsMapping() throws Exception {

        FieldsMappingEntity fieldsMapping = createFieldsMapping();

        Mockito.when(fmRepository.findBySlug(anyString()))
                .thenReturn(Optional.of(fieldsMapping));
        Mockito.when(fmRepository.saveAndFlush(any(FieldsMappingEntity.class)))
                .thenReturn(createFieldsMapping());

        mappingService.update(fieldsMapping);

        Mockito.verify(fmRepository).findBySlug(anyString());
        Mockito.verify(fmRepository).saveAndFlush(any(FieldsMappingEntity.class));
        Mockito.verify(historyRepository).save(any(FieldsMappingHistory.class));

    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void updateFieldsMappingWhenOccursConflict() throws Exception {

        FieldsMappingEntity fieldsMapping = createFieldsMappingUpdateName();

        // return a existent fields mapping
        Mockito.when(fmRepository.findBySlug(anyString()))
                .thenReturn(Optional.of(createFieldsMapping()));

        mappingService.update(fieldsMapping);

    }

    @Test
    public void testUpdateFieldsMappingInexistentEntity() throws Exception {

        FieldsMappingEntity fieldsMapping = createFieldsMapping();

        Mockito.when(fmRepository.findBySlug(anyString()))
                .thenReturn(Optional.empty());

        try {

            mappingService.update(fieldsMapping);
            Assert.fail("EntityNotFoundException expected");

        } catch (EntityNotFoundException e) {

            System.err.println(e.getMessage());

            Mockito.verify(fmRepository).findBySlug(anyString());
            Mockito.verify(fmRepository, Mockito.times(0))
                    .saveAndFlush(any(FieldsMappingEntity.class));
            Mockito.verifyNoMoreInteractions(historyRepository);

        }

    }

    @Test
    public void testFindBySlug() throws Exception {

        Mockito.when(fmRepository.findBySlug(anyString()))
                .thenReturn(Optional.of(createFieldsMapping()));

        mappingService.findBySlug(anyString());

        Mockito.verify(fmRepository).findBySlug(anyString());

    }

    @Test(expected = EntityNotFoundException.class)
    public void testFindBySlugNotFound() throws Exception {

        Mockito.when(fmRepository.findBySlug(anyString()))
                .thenReturn(Optional.empty());

        mappingService.findBySlug(anyString());

    }

    private FieldsMappingEntity createFieldsMapping() {
        MappedFieldEntity mappedField = MappedFieldEntity.builder()
                .partnerField("nome")
                .wmField("name")
                .required(false)
                .build();

        ArrayList<MappedFieldEntity> mappedFields = new ArrayList<>();
        mappedFields.add(mappedField);

        FieldsMappingEntity mappingEntity = FieldsMappingEntity.builder()
                .name("Buscapé")
                .slug("buscape")
                .mappedFields(mappedFields)
                .build();

        return mappingEntity;
    }

    private FieldsMappingEntity createFieldsMappingUpdateName() {
        MappedFieldEntity mappedField = MappedFieldEntity.builder()
                .partnerField("nome")
                .wmField("name")
                .required(false)
                .build();

        FieldsMappingEntity mappingEntity = FieldsMappingEntity.builder()
                .name("Buscapé")
                .slug("zoom")
                .mappedFields(Arrays.asList(mappedField))
                .build();

        return mappingEntity;
    }

}
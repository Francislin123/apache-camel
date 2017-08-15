package com.walmart.feeds.api.unit.core.service.fields;

import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.repository.fields.FieldsMappingHistoryRepository;
import com.walmart.feeds.api.core.repository.fields.FieldsMappingRepository;
import com.walmart.feeds.api.core.repository.fields.model.FieldsMappingEntity;
import com.walmart.feeds.api.core.repository.fields.model.FieldsMappingHistory;
import com.walmart.feeds.api.core.repository.fields.model.MappedFieldEntity;
import com.walmart.feeds.api.core.service.fields.FieldsMappingService;
import com.walmart.feeds.api.core.service.fields.FieldsMappingServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class FieldsMappingServiceTest {

    @Mock
    private FieldsMappingRepository fmRepository;

    @Mock
    private FieldsMappingHistoryRepository historyRepository;

    @InjectMocks
    private FieldsMappingService mappingService = new FieldsMappingServiceImpl();

    @Test
    public void testSaveFieldsdMapping() throws Exception {

        Mockito.when(fmRepository.findBySlug(anyString()))
                .thenReturn(Optional.empty());
        mappingService.save(createFieldsMapping());

        Mockito.verify(fmRepository).findBySlug(anyString());
        Mockito.verify(fmRepository).saveAndFlush(any(FieldsMappingEntity.class));
        Mockito.verify(historyRepository).saveAndFlush(any(FieldsMappingHistory.class));

    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void testSaveFieldsdMappingDuplicatedConstraint() throws Exception {

        Mockito.when(fmRepository.findBySlug(anyString()))
                .thenReturn(Optional.of(createFieldsMapping()));

        mappingService.save(createFieldsMapping());
        Mockito.verify(fmRepository).findBySlug(Mockito.anyString());

        Mockito.verifyNoMoreInteractions(historyRepository);

    }

    @Test
    public void updateFieldsMapping() throws Exception {

        FieldsMappingEntity fieldsMapping = createFieldsMapping();

        Mockito.when(fmRepository.findBySlug(anyString()))
                .thenReturn(Optional.of(fieldsMapping));

        mappingService.update(fieldsMapping);

        Mockito.verify(fmRepository).findBySlug(anyString());
        Mockito.verify(fmRepository).saveAndFlush(any(FieldsMappingEntity.class));
        Mockito.verify(historyRepository).saveAndFlush(any(FieldsMappingHistory.class));

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

        FieldsMappingEntity mappingEntity = FieldsMappingEntity.builder()
                .name("Buscapé")
                .slug("buscape")
                .mappedFields(Arrays.asList(mappedField))
                .build();

        return mappingEntity;
    }

}
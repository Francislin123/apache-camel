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
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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

    public static final List<String> WM_FIELDS;

    static {
        WM_FIELDS = Arrays.asList(
                "offers.seller.name",
                "offers.seller.id",
                "offers.listprice",
                "offers.quantity",
                "offers.price",
                "offers.active",
                "image.thumb",
                "image.main",
                "gtin",
                "product.image.thumb",
                "product.image.main",
                "product.active",
                "product.id",
                "product.title",
                "product.url.web",
                "product.url.mobile",
                "active",
                "specification.dimension.weight",
                "title",
                "url.web",
                "url.mobile",
                "skuWalmart",
                "lastUpdate",
                "categories.depth",
                "categories.name",
                "categories.active",
                "categories.id",
                "id",
                "brand.name",
                "brand.id");
    }

    @Before
    public void init() {

        Mockito.when(elasticSearchComponent.getWalmartFields()).thenReturn(WM_FIELDS);
    }

    @Test
    public void testSaveFieldsMapping() throws Exception {

        FieldsMappingEntity fieldsMapping = createFieldsMapping();

        Mockito.when(fmRepository.findBySlug(anyString())).thenReturn(Optional.empty());
        Mockito.when(fmRepository.saveAndFlush(any(FieldsMappingEntity.class))).thenReturn(createFieldsMapping());

        mappingService.save(createFieldsMapping());

        Mockito.verify(fmRepository).findBySlug(anyString());
        Mockito.verify(fmRepository).saveAndFlush(any(FieldsMappingEntity.class));
        Mockito.verify(historyRepository).save(any(FieldsMappingHistory.class));

    }

    @Test
    public void testSaveFieldsMappingWhenWalmartFieldNotExist() throws Exception {

        Mockito.when(fmRepository.findBySlug(anyString()))
                .thenReturn(Optional.empty());

        try {

            FieldsMappingEntity fieldsMapping = createFieldsMapping();
            fieldsMapping.getMappedFields()
                    .add(MappedFieldEntity.builder().wmField("price").build());

            mappingService.save(fieldsMapping);

            Assert.fail(UserException.class.getName() + " expected");

        } catch (UserException e) {

            assertNotNull(e.getErrors());
            assertFalse(e.getErrors().isEmpty());
            assertTrue(e.getErrors().stream().anyMatch(error -> "price".equals(error.getMessage())));
            Mockito.verify(fmRepository).findBySlug(anyString());
            Mockito.verify(fmRepository, Mockito.times(0)).saveAndFlush(any(FieldsMappingEntity.class));
            Mockito.verify(historyRepository, Mockito.times(0)).saveAndFlush(any(FieldsMappingHistory.class));

        }

    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void testSaveFieldsMappingDuplicatedConstraint() throws Exception {

        Mockito.when(fmRepository.findBySlug(anyString())).thenReturn(Optional.of(createFieldsMapping()));

        mappingService.save(createFieldsMapping());

    }

    @Test
    public void testUpdateFieldsMapping() throws Exception {

        FieldsMappingEntity fieldsMapping = createFieldsMapping();

        Mockito.when(fmRepository.findBySlug(anyString())).thenReturn(Optional.of(fieldsMapping));
        Mockito.when(fmRepository.saveAndFlush(any(FieldsMappingEntity.class))).thenReturn(createFieldsMapping());

        mappingService.update(fieldsMapping);

        Mockito.verify(fmRepository).findBySlug(anyString());
        Mockito.verify(fmRepository).saveAndFlush(any(FieldsMappingEntity.class));
        Mockito.verify(historyRepository).save(any(FieldsMappingHistory.class));

    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void updateFieldsMappingWhenOccursConflict() throws Exception {

        FieldsMappingEntity fieldsMapping = createFieldsMappingUpdateName();

        // return a existent fields mapping
        Mockito.when(fmRepository.findBySlug(anyString())).thenReturn(Optional.of(createFieldsMapping()));

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

            Mockito.verify(fmRepository).findBySlug(anyString());
            Mockito.verify(fmRepository, Mockito.times(0)).saveAndFlush(any(FieldsMappingEntity.class));
            Mockito.verifyNoMoreInteractions(historyRepository);

        }

    }

    @Test
    public void testFindBySlug() throws Exception {

        Mockito.when(fmRepository.findBySlug(anyString())).thenReturn(Optional.of(createFieldsMapping()));

        mappingService.findBySlug(anyString());

        Mockito.verify(fmRepository).findBySlug(anyString());

    }

    @Test(expected = EntityNotFoundException.class)
    public void testFindBySlugNotFound() throws Exception {

        Mockito.when(fmRepository.findBySlug(anyString())).thenReturn(Optional.empty());

        mappingService.findBySlug(anyString());

    }

    private FieldsMappingEntity createFieldsMapping() {
        MappedFieldEntity mappedField = MappedFieldEntity.builder()
                .partnerField("nome")
                .wmField("title")
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
                .wmField("title")
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
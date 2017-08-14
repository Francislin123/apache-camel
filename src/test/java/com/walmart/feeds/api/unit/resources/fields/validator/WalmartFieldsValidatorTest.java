package com.walmart.feeds.api.unit.resources.fields.validator;

import com.walmart.feeds.api.core.repository.fields.model.MappedFieldEntity;
import com.walmart.feeds.api.resources.fields.request.FieldsMappingRequest;
import com.walmart.feeds.api.resources.fields.validator.WalmartFieldsValidator;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WalmartFieldsValidatorTest {

    private static Validator validator;
    private WalmartFieldsValidator fieldsValidator = new WalmartFieldsValidator();

    @BeforeClass
    public static void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void testInvalidateWhenHasNonexistentField() {

        List<MappedFieldEntity> mappedFields = new ArrayList<>();

        mappedFields.add(MappedFieldEntity.builder().wmField("abcd").partnerField("test").build());
        mappedFields.add(MappedFieldEntity.builder().wmField("nonexistent").partnerField("test").build());
        mappedFields.add(MappedFieldEntity.builder().wmField("active").partnerField("test").build());
        mappedFields.add(MappedFieldEntity.builder().wmField("categories.name").partnerField("test").build());

        Set<ConstraintViolation<FieldsMappingRequest>> violations = validator.validate(getRequest(mappedFields));

        violations.forEach(constraint ->
            System.out.println(constraint.getMessage())
        );

        ConstraintViolation<FieldsMappingRequest> constraint = violations.stream().findFirst().get();

        assertFalse(violations.isEmpty());
        assertTrue(constraint.getMessage().contains("The walmart fields does not exists"));
    }

    @Test
    public void testInvalidateWhenHasNullFields() {

        List<MappedFieldEntity> mappedFields = new ArrayList<>();

        mappedFields.add(MappedFieldEntity.builder().wmField(null).partnerField("test").build());
        mappedFields.add(MappedFieldEntity.builder().wmField("nonexistent").partnerField("test").build());
        mappedFields.add(MappedFieldEntity.builder().wmField("active").partnerField("test").build());
        mappedFields.add(MappedFieldEntity.builder().wmField("categories.name").partnerField("test").build());

        Set<ConstraintViolation<FieldsMappingRequest>> violations = validator.validate(getRequest(mappedFields));

        violations.forEach(constraint -> {
            System.out.println(constraint.getMessage());
            System.out.println(constraint.getInvalidValue());
        });

        assertFalse(violations.isEmpty());

    }

    @Test
    public void testInvalidateWhenHasEmptyMappedFields() {

        List<MappedFieldEntity> mappedFields = new ArrayList<>();

        Set<ConstraintViolation<FieldsMappingRequest>> violations = validator.validate(getRequest(mappedFields));

        violations.forEach(constraint ->
            System.out.println(constraint.getMessage())
        );

        ConstraintViolation<FieldsMappingRequest> constraint = violations.stream().findFirst().get();

        assertFalse(violations.isEmpty());
        assertTrue(constraint.getMessage().contains("may not be empty"));

    }

    private FieldsMappingRequest getRequest(List<MappedFieldEntity> mappedFields) {
        return FieldsMappingRequest.builder()
                .name("test").mappedFields(mappedFields)
                .build();
    }

}

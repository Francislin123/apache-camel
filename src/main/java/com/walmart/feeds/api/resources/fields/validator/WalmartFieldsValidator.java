package com.walmart.feeds.api.resources.fields.validator;

import com.walmart.feeds.api.core.repository.fields.model.MappedFieldEntity;
import com.walmart.feeds.api.persistence.ElasticSearchComponent;
import com.walmart.feeds.api.resources.fields.validator.annotation.ValidWalmartFields;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;

public class WalmartFieldsValidator implements ConstraintValidator<ValidWalmartFields, List<MappedFieldEntity>> {

    //@Autowired
    private ElasticSearchComponent elasticComponent = new ElasticSearchComponent();

    @Override
    public void initialize(ValidWalmartFields constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<MappedFieldEntity> values, ConstraintValidatorContext context) {

        List<String> walmartFields = elasticComponent.getWalmartFields();

        List<String> notContains = values.stream()
            .filter(field -> !walmartFields.contains(field.getWmField()))
            .map(field -> field.getWmField())
            .collect(Collectors.toList());

        ((ConstraintValidatorContextImpl) context)
                .addExpressionVariable("fieldsList", notContains.toString());

        return notContains.isEmpty();
    }
}

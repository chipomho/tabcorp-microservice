package au.com.tabcorp.toolkit.api.validators;

import au.com.tabcorp.common.enums.ConstraintType;
import au.com.tabcorp.common.model.StandardErrorModel;
import au.com.tabcorp.common.model.ValidationErrorModel;
import au.com.tabcorp.toolkit.api.AbstractTest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;

import java.util.List;

public abstract class AbstractValidatorTest<V extends AbstractValidator<?>> extends AbstractTest {

    @Autowired()
    protected V validator;

    protected boolean hasConstraint(final BindingResult result, final String field, final ConstraintType constraintType){
        final List<ValidationErrorModel> errors = validator.build(result);
        final String code = validator.code(field,constraintType);
        return CollectionUtils.isNotEmpty(errors) && (errors.stream().filter(e-> StringUtils.equals(e.getCode(), code)).count()>0 );
    }

    @Order(1)
    @Test()
    @DisplayName("Should successfully load Application Context")
    void shouldSuccessfullyLoadApplicationContext() {
        Assertions.assertNotNull(validator,"Model Validator is NULL");
    }

}

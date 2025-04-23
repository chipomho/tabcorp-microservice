package au.com.tabcorp.toolkit.api.validators;

import au.com.tabcorp.common.enums.ConstraintType;
import au.com.tabcorp.common.model.ProductModel;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BindingResult;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@DisplayName("Product Model Validator")
class ProductModelValidatorTest extends AbstractValidatorTest<ProductModelValidator> {

    @Order(2)
    @Test()
    @DisplayName("Should successfully validate Create Product Model")
    void shouldSuccessfullyValidateCreateCustomerModel() {

        final ProductModel model = productModel();
        Mono<BindingResult> result = validator.validateCreate(model);
        when(productFacade.product(model.getCode())).thenReturn(Mono.empty());

        StepVerifier.create(result)
                .expectNextMatches(c -> !c.hasErrors())
                .verifyComplete();

        verify(productFacade, times(1)).product(model.getCode());
    }

    @Order(3)
    @Test()
    @DisplayName("Should fail validation if Product Model 'Code' is blank")
    void shouldFailValidationIfProductModelCodeIsBlank() {

        final ProductModel model = productModel();
        model.setCode(null);
        Mono<BindingResult> result = validator.validateCreate(model);

        StepVerifier.create(result)
                .expectNextMatches(errors -> hasConstraint( errors, "code", ConstraintType.BLANK))
                .verifyComplete();

    }

    @Order(4)
    @Test()
    @DisplayName("Should fail validation if Product Model 'Code' is too long")
    void shouldFailValidationIfProductModelCodeIsTooLong() {

        final ProductModel model = productModel();
        model.setCode(RandomStringUtils.random(ProductModel.MAXIMUM_LENGTH_PRODUCT_CODE)+10);
        Mono<BindingResult> result = validator.validateCreate(model);

        StepVerifier.create(result)
                .expectNextMatches(errors -> hasConstraint( errors, "code", ConstraintType.LENGTH))
                .verifyComplete();

    }

    @Order(5)
    @Test()
    @DisplayName("Should fail validation if Product Model 'Status' is null")
    void shouldFailValidationIfProductModelStatusIsNull() {

        final ProductModel model = productModel();
        model.setStatus(null);
        Mono<BindingResult> result = validator.validateCreate(model);

        StepVerifier.create(result)
                .expectNextMatches(errors -> hasConstraint( errors, "status", ConstraintType.NULL))
                .verifyComplete();

    }

    @Order(6)
    @Test()
    @DisplayName("Should fail validation if Product Model 'Cost' is null")
    void shouldFailValidationIfProductModelCostIsNull() {

        final ProductModel model = productModel();
        model.setCost(null);
        Mono<BindingResult> result = validator.validateCreate(model);

        StepVerifier.create(result)
                .expectNextMatches(errors -> hasConstraint( errors, "cost", ConstraintType.NULL))
                .verifyComplete();

    }

    @Order(7)
    @Test()
    @DisplayName("Should fail validation if Product Model 'Cost' is not valid")
    void shouldFailValidationIfProductModelCostIsNotValid() {

        final ProductModel model = productModel();
        model.setCost(new BigDecimal("-3.00"));
        Mono<BindingResult> result = validator.validateCreate(model);

        StepVerifier.create(result)
                .expectNextMatches(errors -> hasConstraint( errors, "cost", ConstraintType.VALID))
                .verifyComplete();

    }

    @Order(7)
    @Test()
    @DisplayName("Should fail create validation if Product Model 'Code' already exists")
    void shouldFailCreateValidationIfProductModelCodeAlreadyExists() {

        final ProductModel model = productModel();
        Mono<BindingResult> result = validator.validateCreate(model);
        when(productFacade.product(model.getCode())).thenReturn(Mono.just(model));

        StepVerifier.create(result)
                .expectNextMatches(errors -> hasConstraint( errors, "code", ConstraintType.EXISTS))
                .verifyComplete();

        verify(productFacade,times(1)).product(model.getCode());

    }

    @Order(8)
    @Test()
    @DisplayName("Should fail Update validation if Product Model 'Code' does not exist")
    void shouldFailUpdateValidationIfProductModelCodeDoesNotExist() {
        final ProductModel model = productModel();
        Mono<BindingResult> result = validator.validateUpdate(model);
        when(productFacade.product(model.getCode())).thenReturn(Mono.empty());

        StepVerifier.create(result)
                .expectNextMatches(errors -> hasConstraint( errors, "code", ConstraintType.VALID))
                .verifyComplete();

        verify(productFacade,times(1)).product(model.getCode());

    }
}
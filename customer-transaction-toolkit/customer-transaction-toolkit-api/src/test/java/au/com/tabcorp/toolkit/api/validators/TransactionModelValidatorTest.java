package au.com.tabcorp.toolkit.api.validators;

import au.com.tabcorp.common.enums.ConstraintType;
import au.com.tabcorp.common.enums.ProductStatus;
import au.com.tabcorp.common.model.CustomerModel;
import au.com.tabcorp.common.model.ProductModel;
import au.com.tabcorp.common.model.TransactionModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BindingResult;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@DisplayName("Transaction Model Validator")
class TransactionModelValidatorTest extends AbstractValidatorTest<TransactionModelValidator> {


    @Order(2)
    @Test()
    @DisplayName("Should successfully validate Transaction Model")
    void shouldSuccessfullyValidateCreateCustomerModel() {

        final TransactionModel model = transactionModel();
        final ProductModel product = productModel();
        final CustomerModel customer = customerModel();

        model.setProductCode(product.getCode());
        model.setCustomerId(customer.getId());
        model.setQuantity(1);

        when(customerFacade.customer(model.getCustomerId())).thenReturn(Mono.just(customer));
        when(productFacade.product(product.getCode())).thenReturn(Mono.just(product));

        Mono<BindingResult> result = validator.validate(model);

        StepVerifier.create(result)
                .expectNextMatches(c -> !c.hasErrors())
                .verifyComplete();

        verify(productFacade, times(1)).product(product.getCode());
        verify(customerFacade, times(1)).customer(model.getCustomerId());

    }

    @Order(3)
    @Test()
    @DisplayName("Should fail if Transaction Model 'Transaction Time' is null")
    void shouldFailIfTransactionModelTransactionTimeIsNull() {

        final TransactionModel model = transactionModel();
        final ProductModel product = productModel();
        final CustomerModel customer = customerModel();

        model.setProductCode(product.getCode());
        model.setCustomerId(customer.getId());
        model.setQuantity(1);

        model.setTransactionTime(null);

        when(customerFacade.customer(model.getCustomerId())).thenReturn(Mono.just(customer));
        when(productFacade.product(product.getCode())).thenReturn(Mono.just(product));

        Mono<BindingResult> result = validator.validate(model);

        StepVerifier.create(result)
                .expectNextMatches(errors -> hasConstraint(errors, "transactionTime", ConstraintType.NULL))
                .verifyComplete();

        verify(productFacade, never()).product(product.getCode());
        verify(customerFacade, never()).customer(model.getCustomerId());

    }

    @Order(4)
    @Test()
    @DisplayName("Should fail if Transaction Model 'Transaction Time' is in the past")
    void shouldFailIfTransactionModelTransactionTimeIsInThePast() {

        final TransactionModel model = transactionModel();
        final ProductModel product = productModel();
        final CustomerModel customer = customerModel();

        model.setProductCode(product.getCode());
        model.setCustomerId(customer.getId());
        model.setQuantity(1);

        model.setTransactionTime(LocalDateTime.now().minusDays(1));

        when(customerFacade.customer(model.getCustomerId())).thenReturn(Mono.just(customer));
        when(productFacade.product(product.getCode())).thenReturn(Mono.just(product));

        Mono<BindingResult> result = validator.validate(model);

        StepVerifier.create(result)
                .expectNextMatches(errors -> hasConstraint(errors, "transactionTime", ConstraintType.VALID))
                .verifyComplete();

        verify(productFacade, never()).product(product.getCode());
        verify(customerFacade, never()).customer(model.getCustomerId());
    }

    @Order(5)
    @Test()
    @DisplayName("Should fail if Transaction Model 'Quantity' is null or less than zero")
    void shouldFailIfTransactionModelQuantityIsNullOrLessThanZero() {

        final TransactionModel model = transactionModel();
        final ProductModel product = productModel();
        final CustomerModel customer = customerModel();

        model.setProductCode(product.getCode());
        model.setCustomerId(customer.getId());
        model.setQuantity(1);

        model.setQuantity(0);

        when(customerFacade.customer(model.getCustomerId())).thenReturn(Mono.just(customer));
        when(productFacade.product(product.getCode())).thenReturn(Mono.just(product));

        Mono<BindingResult> result = validator.validate(model);

        StepVerifier.create(result)
                .expectNextMatches(errors -> hasConstraint(errors, "quantity", ConstraintType.NULL))
                .verifyComplete();

        verify(productFacade, never()).product(product.getCode());
        verify(customerFacade, never()).customer(model.getCustomerId());

    }

    @Order(6)
    @Test()
    @DisplayName("Should fail if Transaction Model 'Product Code' is blank")
    void shouldFailIfTransactionModelProductCodeIsBlank() {

        final TransactionModel model = transactionModel();
        final ProductModel product = productModel();
        final CustomerModel customer = customerModel();

        model.setCustomerId(customer.getId());
        model.setQuantity(1);

        model.setProductCode(null);

        when(customerFacade.customer(model.getCustomerId())).thenReturn(Mono.just(customer));
        when(productFacade.product(product.getCode())).thenReturn(Mono.just(product));

        Mono<BindingResult> result = validator.validate(model);

        StepVerifier.create(result)
                .expectNextMatches(errors -> hasConstraint(errors, "productCode", ConstraintType.BLANK))
                .verifyComplete();

        verify(productFacade, never()).product(product.getCode());
        verify(customerFacade, never()).customer(model.getCustomerId());

    }

    @Order(7)
    @Test()
    @DisplayName("Should fail if Transaction Model 'Customer Id' is null or Less Than Zero")
    void shouldFailIfTransactionModelCustomerIdIsLessThanZeroOrNull() {

        final TransactionModel model = transactionModel();
        final ProductModel product = productModel();
        final CustomerModel customer = customerModel();

        model.setProductCode(product.getCode());
        model.setCustomerId(customer.getId());
        model.setQuantity(1);

        model.setCustomerId(0L);

        when(customerFacade.customer(model.getCustomerId())).thenReturn(Mono.just(customer));
        when(productFacade.product(product.getCode())).thenReturn(Mono.just(product));

        Mono<BindingResult> result = validator.validate(model);

        StepVerifier.create(result)
                .expectNextMatches(errors -> hasConstraint(errors, "customerId", ConstraintType.NULL))
                .verifyComplete();

        verify(productFacade, never()).product(product.getCode());
        verify(customerFacade, never()).customer(model.getCustomerId());

    }

    @Order(8)
    @Test()
    @DisplayName("Should fail if Transaction Model 'Customer Id' is not valid")
    void shouldFailIfTransactionModelCustomerIdIsNotValid() {

        final TransactionModel model = transactionModel();
        final ProductModel product = productModel();
        final CustomerModel customer = customerModel();

        model.setProductCode(product.getCode());
        model.setCustomerId(customer.getId());
        model.setQuantity(1);

        when(customerFacade.customer(model.getCustomerId())).thenReturn(Mono.empty());
        when(productFacade.product(product.getCode())).thenReturn(Mono.just(product));

        Mono<BindingResult> result = validator.validate(model);

        StepVerifier.create(result)
                .expectNextMatches(errors -> hasConstraint(errors, "customerId", ConstraintType.MATCH))
                .verifyComplete();

        verify(productFacade, never()).product(product.getCode());
        verify(customerFacade, times(1)).customer(model.getCustomerId());

    }

    @Order(9)
    @Test()
    @DisplayName("Should fail if Transaction Model 'Product Code' does not exist")
    void shouldFailIfTransactionModelProductCodeDoesNotExist() {

        final TransactionModel model = transactionModel();
        final ProductModel product = productModel();
        final CustomerModel customer = customerModel();

        model.setProductCode(product.getCode());
        model.setCustomerId(customer.getId());
        model.setQuantity(1);

        when(customerFacade.customer(model.getCustomerId())).thenReturn(Mono.just(customer));
        when(productFacade.product(product.getCode())).thenReturn(Mono.empty());

        Mono<BindingResult> result = validator.validate(model);

        StepVerifier.create(result)
                .expectNextMatches(errors -> hasConstraint(errors, "productCode", ConstraintType.MATCH))
                .verifyComplete();

        verify(productFacade, times(1)).product(product.getCode());
        verify(customerFacade, times(1)).customer(model.getCustomerId());

    }

    @Order(10)
    @Test()
    @DisplayName("Should fail if Transaction Model 'Product Code' is not Active")
    void shouldFailIfTransactionModelProductCodeIsNotActive() {

        final TransactionModel model = transactionModel();
        final ProductModel product = productModel();
        final CustomerModel customer = customerModel();

        model.setProductCode(product.getCode());
        model.setCustomerId(customer.getId());
        model.setQuantity(1);

        product.setStatus(ProductStatus.INACTIVE);

        when(customerFacade.customer(model.getCustomerId())).thenReturn(Mono.just(customer));
        when(productFacade.product(product.getCode())).thenReturn(Mono.just(product));

        Mono<BindingResult> result = validator.validate(model);

        StepVerifier.create(result)
                .expectNextMatches(errors -> hasConstraint(errors, "productCode", ConstraintType.STATUS))
                .verifyComplete();

        verify(productFacade, times(1)).product(product.getCode());
        verify(customerFacade, times(1)).customer(model.getCustomerId());

    }

    @Order(11)
    @Test()
    @DisplayName("Should fail if Transaction Model if cost is above the configured value")
    void shouldFailIfTransactionModelCostIsAboveTheConfiguredValue() {

        final TransactionModel model = transactionModel();
        final ProductModel product = productModel();
        final CustomerModel customer = customerModel();

        model.setProductCode(product.getCode());
        model.setCustomerId(customer.getId());
        model.setQuantity(  (int)(validator.maximumCost * 2) );

        when(customerFacade.customer(model.getCustomerId())).thenReturn(Mono.just(customer));
        when(productFacade.product(product.getCode())).thenReturn(Mono.just(product));

        Mono<BindingResult> result = validator.validate(model);

        StepVerifier.create(result)
                .expectNextMatches(errors -> hasConstraint(errors, "productCode", ConstraintType.MAXIMUM))
                .verifyComplete();

        verify(productFacade, times(1)).product(product.getCode());
        verify(customerFacade, times(1)).customer(model.getCustomerId());

    }

}

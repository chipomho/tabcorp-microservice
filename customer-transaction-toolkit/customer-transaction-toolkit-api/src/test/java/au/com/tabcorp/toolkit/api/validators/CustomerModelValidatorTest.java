package au.com.tabcorp.toolkit.api.validators;

import au.com.tabcorp.common.enums.ConstraintType;
import au.com.tabcorp.common.model.CustomerModel;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BindingResult;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@DisplayName("Customer Model Validator")
class CustomerModelValidatorTest extends AbstractValidatorTest<CustomerModelValidator> {

    @Order(2)
    @Test()
    @DisplayName("Should successfully validate Create Customer Model")
    void shouldSuccessfullyValidateCreateCustomerModel() {

        final CustomerModel model = customerModel();
        Mono<BindingResult> result = validator.validateCreate(model);

        StepVerifier.create(result)
                .expectNextMatches(c -> !c.hasErrors())
                .verifyComplete();

    }

    @Order(3)
    @Test()
    @DisplayName("Should fail validation if Customer Model 'First Name' is blank")
    void shouldFailValidationIfFirstNameIsBlank() {

        final CustomerModel model = customerModel();
        model.setFirstName(null);
        Mono<BindingResult> result = validator.validateCreate(model);

        StepVerifier.create(result)
                .expectNextMatches(errors -> hasConstraint(errors, "firstName", ConstraintType.BLANK))
                .verifyComplete();

    }

    @Order(4)
    @Test()
    @DisplayName("Should fail validation if Customer Model 'Last Name' is blank")
    void shouldFailValidationIfLastNameIsBlank() {

        final CustomerModel model = customerModel();
        model.setLastName(null);
        Mono<BindingResult> result = validator.validateCreate(model);

        StepVerifier.create(result)
                .expectNextMatches(errors -> hasConstraint( errors, "lastName", ConstraintType.BLANK))
                .verifyComplete();

    }

    @Order(5)
    @Test()
    @DisplayName("Should fail validation if Customer Model 'Email Address' is blank")
    void shouldFailValidationIfEmailAddressIsBlank() {

        final CustomerModel model = customerModel();
        model.setEmailAddress(null);
        Mono<BindingResult> result = validator.validateCreate(model);

        StepVerifier.create(result)
                .expectNextMatches(errors -> hasConstraint( errors, "emailAddress", ConstraintType.BLANK))
                .verifyComplete();

    }

    @Order(6)
    @Test()
    @DisplayName("Should fail validation if Customer Model 'Location' is blank")
    void shouldFailValidationIfLocationIsBlank() {

        final CustomerModel model = customerModel();
        model.setLocation(null);
        Mono<BindingResult> result = validator.validateCreate(model);

        StepVerifier.create(result)
                .expectNextMatches(errors -> hasConstraint( errors, "location", ConstraintType.BLANK))
                .verifyComplete();

    }

    @Order(7)
    @Test()
    @DisplayName("Should fail validation if Customer Model 'First Name' is too long")
    void shouldFailValidationIfFirstNameIsTooLong() {

        final CustomerModel model = customerModel();
        model.setFirstName(RandomStringUtils.randomAlphabetic(CustomerModel.MAXIMUM_LENGTH_CUSTOMER_FIRST_NAME)+10);
        Mono<BindingResult> result = validator.validateCreate(model);

        StepVerifier.create(result)
                .expectNextMatches(errors -> hasConstraint( errors, "firstName", ConstraintType.LENGTH))
                .verifyComplete();

    }

    @Order(8)
    @Test()
    @DisplayName("Should fail validation if Customer Model 'Last Name' is too long")
    void shouldFailValidationIfLastNameIsTooLong() {

        final CustomerModel model = customerModel();
        model.setLastName(RandomStringUtils.randomAlphabetic(CustomerModel.MAXIMUM_LENGTH_CUSTOMER_LAST_NAME)+10);
        Mono<BindingResult> result = validator.validateCreate(model);

        StepVerifier.create(result)
                .expectNextMatches(errors -> hasConstraint( errors, "lastName", ConstraintType.LENGTH))
                .verifyComplete();

    }

    @Order(9)
    @Test()
    @DisplayName("Should fail validation if Customer Model 'Email Address' is too long")
    void shouldFailValidationIfEmailAddressIsTooLong() {

        final CustomerModel model = customerModel();
        model.setEmailAddress(RandomStringUtils.randomAlphabetic(CustomerModel.MAXIMUM_LENGTH_CUSTOMER_EMAIL_ADDRESS)+"@gmail.com");
        Mono<BindingResult> result = validator.validateCreate(model);

        StepVerifier.create(result)
                .expectNextMatches(errors -> hasConstraint( errors, "emailAddress", ConstraintType.LENGTH))
                .verifyComplete();

    }

    @Order(10)
    @Test()
    @DisplayName("Should fail validation if Customer Model 'Location' is too long")
    void shouldFailValidationIfLocationIsTooLong() {

        final CustomerModel model = customerModel();
        model.setLocation(RandomStringUtils.randomAlphabetic(CustomerModel.MAXIMUM_LENGTH_CUSTOMER_LOCATION)+10);
        Mono<BindingResult> result = validator.validateCreate(model);

        StepVerifier.create(result)
                .expectNextMatches(errors -> hasConstraint( errors, "location", ConstraintType.LENGTH))
                .verifyComplete();

    }

    @Order(11)
    @Test()
    @DisplayName("Should successfully validate Update Customer Model")
    void shouldSuccessfullyValidateUpdateCustomerModel() {

        final CustomerModel model = customerModel();

        when(customerFacade.customer(model.getId())).thenReturn(Mono.just(model));
        //when(customerFacade.updateCustomer(model)).thenReturn(Mono.just(model));

        Mono<BindingResult> result = validator.validateUpdate(model);

        StepVerifier.create(result)
                .expectNextMatches(errors -> !errors.hasErrors())
                .verifyComplete();

        verify(customerFacade, times(1)).customer(model.getId());
        //verify(customerFacade,times(1)).updateCustomer(model);

    }

}
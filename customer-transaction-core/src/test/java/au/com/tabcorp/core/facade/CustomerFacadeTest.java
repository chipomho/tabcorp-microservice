package au.com.tabcorp.core.facade;

import au.com.tabcorp.common.model.CustomerModel;
import au.com.tabcorp.data.entity.Customer;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Customer Facade")
class CustomerFacadeTest extends AbstractFacadeTest{

    @Autowired()
    protected CustomerFacade customerFacade;

    @Order(1)
    @Test()
    @DisplayName("Should successfully load Application Context")
    void shouldSuccessfullyLoadApplicationContext() {
      assertNotNull(customerFacade,"CustomerFacade is null");
    }


    @Order(2)
    @Test()
    @DisplayName("Should successfully return Customer instance if it exists")
    void shouldSuccessfullyReturnCustomerInstanceIfItExists() {
        final CustomerModel model = customerModel();
        final String key = CustomerFacade.customerId(model.getId());
        when(customerService.customer(model.getId())).thenReturn(Mono.just(customer(model)));
        when(cacheService.get(key,CustomerModel.class)).thenReturn(Mono.empty());
        when(cacheService.set(any(String.class),any(CustomerModel.class) )).thenReturn(Mono.just(Boolean.TRUE));

        final Mono<CustomerModel> result = customerFacade.customer(model.getId());
        StepVerifier.create(result)
                .expectNextMatches(customer -> Objects.equals(customer.getId(),model.getId())
                && StringUtils.equalsIgnoreCase(customer.getEmailAddress(), model.getEmailAddress()))
                .verifyComplete();

        verify(customerService,atMostOnce()).customer(model.getId());
        verify(cacheService,atMostOnce()).get(key,CustomerModel.class);
        verify(cacheService,atMostOnce()).set(any(String.class),any(CustomerModel.class));
    }

    @Order(3)
    @Test()
    @DisplayName("Should return NULL Customer instance if it does not exist")
    void shouldReturnNullCustomerInstanceIfItDoesNotExists() {
        final CustomerModel model = customerModel();
        final String key = CustomerFacade.customerId(model.getId());
        when(cacheService.get(key,CustomerModel.class)).thenReturn(Mono.empty());
        when(customerService.customer(model.getId())).thenReturn(Mono.empty());

        final Mono<CustomerModel> result = customerFacade.customer(model.getId());
        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();

        verify(customerService,atMostOnce()).customer(model.getId());
        verify(cacheService,atMostOnce()).get(key,CustomerModel.class);
    }

    @Order(3)
    @Test()
    @DisplayName("Should successfully create Customer instance")
    void shouldSuccessfullyCreateCustomerInstance() {
        final CustomerModel model = customerModel();
        final Customer entity = customer(model);
        when(customerService.save(entity)).thenReturn(Mono.just(entity));
        when(cacheService.set(any(String.class),any(CustomerModel.class) )).thenReturn(Mono.just(Boolean.TRUE));

        final Mono<CustomerModel> result = customerFacade.createCustomer(model);
        StepVerifier.create(result)
                .expectNextMatches(customer -> Objects.equals(customer.getId(),model.getId())
                        && StringUtils.equalsIgnoreCase(customer.getEmailAddress(), model.getEmailAddress()))
                .verifyComplete();

        verify(customerService,atMostOnce()).save(entity);
        verify(cacheService,atMostOnce()).set(any(String.class),any(CustomerModel.class));
    }

    @Order(4)
    @Test()
    @DisplayName("Should successfully update Customer instance")
    void shouldSuccessfullyUpdateCustomerInstance() {
        final CustomerModel model = customerModel();
        final Customer entity = customer(model);
        when(customerService.update(entity)).thenReturn(Mono.just(entity));
        when(cacheService.set(any(String.class),any(CustomerModel.class) )).thenReturn(Mono.just(Boolean.TRUE));

        final Mono<CustomerModel> result = customerFacade.updateCustomer(model);
        StepVerifier.create(result)
                .expectNextMatches(customer -> Objects.equals(customer.getId(),model.getId())
                        && StringUtils.equalsIgnoreCase(customer.getEmailAddress(), model.getEmailAddress()))
                .verifyComplete();

        verify(customerService,atMostOnce()).update(entity);
        verify(cacheService,atMostOnce()).set(any(String.class),any(CustomerModel.class));
    }
    @Order(5)
    @Test()
    @DisplayName("Should successfully delete Customer instance")
    void shouldSuccessfullyDeleteCustomerInstance() {
        final Long customerId = 2L;
        when(customerService.deleteById(customerId)).thenReturn(Mono.empty());

        final Mono<Void> result = customerFacade.deleteCustomer(customerId);
        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();

        verify(customerService,atMostOnce()).deleteById(customerId);
    }

}
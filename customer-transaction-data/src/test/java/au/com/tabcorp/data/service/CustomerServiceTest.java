package au.com.tabcorp.data.service;

import au.com.tabcorp.data.entity.Customer;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.mockito.Mockito.*;

@DisplayName("Customer Service")
class CustomerServiceTest extends AbstractServiceTest{

    @Autowired()
    private CustomerService customerService;

    @Order(1)
    @Test()
    @DisplayName("Should successfully load Application Context")
    void shouldSuccessfullyLoadApplicationContext() {
        Assertions.assertNotNull(customerService,"Customer Service is NULL");
    }

    @Order(2)
    @Test()
    @DisplayName("Should return Customer by Id if Customer exists")
    void shouldReturnCustomerByIdIfCustomerExists() {
        final Customer customer = customer();
        when(customerRepository.findById(customer.getId())).thenReturn(Mono.just(customer));

        final Mono<Customer> entity = customerService.customer(customer.getId());
        StepVerifier.create(entity)
                .expectNextMatches(c -> StringUtils.equalsIgnoreCase(c.getFirstName(),customer.getFirstName()) &&
                        StringUtils.equalsIgnoreCase(c.getLastName(),customer.getLastName()))
                .verifyComplete();

        verify(customerRepository,times(1)).findById(customer.getId());

    }

    @Order(3)
    @Test()
    @DisplayName("Should return NULL Customer by Id if customer does not exist")
    void shouldReturnNullCustomerByIdIfCustomerDoesNotExist() {
        final Long id = 10L;
        when(customerRepository.findById(id)).thenReturn(Mono.empty());

        final Mono<Customer> customer = customerService.customer(id);
        StepVerifier.create(customer)
                .expectNextCount(0)
                .verifyComplete();

        verify(customerRepository,times(1)).findById(id);

    }


    @Order(4)
    @Test()
    @DisplayName("Should successfully save Customer entity")
    void shouldSuccessfullySaveCustomerEntity() {
        final Customer customer = customer();
        when(customerRepository.save(customer)).thenReturn(Mono.just(customer));

        final Mono<Customer> entity = customerService.save(customer);
        StepVerifier.create(entity)
                .expectNextMatches(c -> StringUtils.equalsIgnoreCase(c.getFirstName(),customer.getFirstName()) &&
                        StringUtils.equalsIgnoreCase(c.getLastName(),customer.getLastName()))
                .verifyComplete();

        verify(customerRepository,times(1)).save(customer);

    }

    @Order(5)
    @Test()
    @DisplayName("Should successfully update Customer entity if it exists")
    void shouldSuccessfullyUpdateCustomerEntity() {
        final Customer customer = customer();
        when(customerRepository.findById(customer.getId())).thenReturn(Mono.just(customer));
        when(customerRepository.save(customer)).thenReturn(Mono.just(customer));

        final Mono<Customer> entity = customerService.update(customer);
        StepVerifier.create(entity)
                .expectNextMatches(c -> StringUtils.equalsIgnoreCase(c.getFirstName(),customer.getFirstName()) &&
                        StringUtils.equalsIgnoreCase(c.getLastName(),customer.getLastName()) && Objects.equals(customer.getId(), c.getId()) )
                .verifyComplete();

        verify(customerRepository, times(1)).findById(customer.getId());
        verify(customerRepository, times(1)).save(customer);
    }

    @Order(6)
    @Test()
    @DisplayName("Should do nothing when update Customer is called and entity does not exist")
    void shouldDoNothingWhenUpdateCustomerIsCalledAndEntityDoesNotExist() {
        final Customer customer = customer();
        when(customerRepository.findById(customer.getId())).thenReturn(Mono.empty());

        final Mono<Customer> entity = customerService.update(customer);
        StepVerifier.create(entity)
                .expectNextCount(0)
                .verifyComplete();

        verify(customerRepository,times(1)).findById(customer.getId());
    }

    @Order(6)
    @Test()
    @DisplayName("Should successfully delete Customer entity")
    void shouldSuccessfullyDeleteCustomerEntity() {
        final Customer customer = customer();
        when(customerRepository.deleteById(customer.getId())).thenReturn(Mono.empty());

        final Mono<Void> entity = customerService.deleteById(customer.getId());
        StepVerifier.create(entity)
                .expectNextCount(0)
                .verifyComplete();

        verify(customerRepository,times(1)).deleteById(customer.getId());
    }
}
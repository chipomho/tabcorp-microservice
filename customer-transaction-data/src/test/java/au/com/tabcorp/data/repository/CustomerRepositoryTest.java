package au.com.tabcorp.data.repository;

import au.com.tabcorp.data.entity.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Check if repositories are loading and executing
 */
@DisplayName("Customer Repository")
class CustomerRepositoryTest extends AbstractRepositoryTest {

    @Autowired()
    private CustomerRepository customerRepository;

    @Order(1)
    @Test()
    @DisplayName("Should successfully start the Application Context")
    void shouldStartApplicationContext() {
      Assertions.assertNotNull(customerRepository, "Customer Repository is null");
    }

    @Order(2)
    @Test()
    @DisplayName("Should successfully save Customer")
    void shouldSuccessfullySaveCustomer() {
        Customer customer = Customer.builder().location("Australia").firstName("First").lastName("Avenger").emailAddress("first-avenger@gmail.com").build();
        final Mono<Customer> savedCustomer = customerRepository.save(customer);
        StepVerifier.create(savedCustomer)
                .expectNextMatches(c -> c.getFirstName().equals("First") && c.getLastName().equals("Avenger"))
                .verifyComplete();
    }


}
package au.com.tabcorp.data.repository;

import au.com.tabcorp.data.entity.Customer;
import au.com.tabcorp.data.entity.CustomerTransaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

/**
 * Check if repositories are loading and executing
 */
@DisplayName("Customer Transaction Repository")
class CustomerTransactionRepositoryTest extends AbstractRepositoryTest {

    @Autowired()
    private CustomerTransactionRepository customerTransactionRepository;

    @Order(1)
    @Test()
    @DisplayName("Should successfully start the Application Context")
    void shouldStartApplicationContext() {
      Assertions.assertNotNull(customerTransactionRepository, "Customer Transaction Repository is null");
    }

    @Order(2)
    @Test()
    @DisplayName("Should successfully save Customer Transaction")
    void shouldSuccessfullySaveCustomer() {
        CustomerTransaction customer = CustomerTransaction.builder().customerId(100L).transactionTime(LocalDateTime.now().plusDays(1))
                .productCode("PRODUCT_001").quantity(3).build();
        final Mono<CustomerTransaction> savedCustomer = customerTransactionRepository.save(customer);
        StepVerifier.create(savedCustomer)
                .expectNextMatches(c -> c.getProductCode().equals(customer.getProductCode()) && c.getCustomerId().equals(customer.getCustomerId()))
                .verifyComplete();
    }


}
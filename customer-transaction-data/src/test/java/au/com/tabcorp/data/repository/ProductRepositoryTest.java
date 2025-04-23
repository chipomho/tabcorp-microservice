package au.com.tabcorp.data.repository;

import au.com.tabcorp.common.enums.ProductStatus;
import au.com.tabcorp.data.entity.Customer;
import au.com.tabcorp.data.entity.Product;
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
@DisplayName("Product Repository")
class ProductRepositoryTest extends AbstractRepositoryTest {

    @Autowired()
    private ProductRepository productRepository;

    @Order(1)
    @Test()
    @DisplayName("Should successfully start the Application Context")
    void shouldStartApplicationContext() {
      Assertions.assertNotNull(productRepository, "Product Repository is null");
    }

    @Order(2)
    @Test()
    @DisplayName("Should successfully retrieve Product")
    void shouldSuccessfullyR() {
        String code = "PRODUCT_001";
        final Mono<Product> savedCustomer = productRepository.findById(code);
        StepVerifier.create(savedCustomer)
                .expectNextMatches(c -> c.getCode().equals(code) && c.getStatus().equals(ProductStatus.ACTIVE))
                .verifyComplete();
    }


}
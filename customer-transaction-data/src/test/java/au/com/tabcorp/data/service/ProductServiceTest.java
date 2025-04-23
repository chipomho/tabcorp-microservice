package au.com.tabcorp.data.service;

import au.com.tabcorp.data.entity.Product;
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

@DisplayName("Product Service")
class ProductServiceTest extends AbstractServiceTest {

    @Autowired()
    private ProductService productService;

    @Order(1)
    @Test()
    @DisplayName("Should successfully load Application Context")
    void shouldSuccessfullyLoadApplicationContext() {
        Assertions.assertNotNull(productService,"Product Service is NULL");
    }

    @Order(2)
    @Test()
    @DisplayName("Should successfully return Product by code if it exists")
    void shouldSuccessfullyReturnProductByCode() {
       Product product = product();
       when(productRepository.findById(product.getCode())).thenReturn(Mono.just(product));

       final Mono<Product> result = productService.getProductByCode(product.getCode());
        StepVerifier.create(result)
                .expectNextMatches(c -> StringUtils.equalsIgnoreCase(c.getCode(),product.getCode()) &&
                        Objects.equals(c.getCost(),product.getCost()))
                .verifyComplete();


       verify(productRepository,times(1)).findById(product.getCode());
    }

    @Order(3)
    @Test()
    @DisplayName("Should return NULL or Empty Product by code if it does not exist")
    void shouldReturnEmptyProductByCodeIfItDoesNotExist() {
        Product product = product();
        when(productRepository.findById(product.getCode())).thenReturn(Mono.empty());

        final Mono<Product> result = productService.getProductByCode(product.getCode());
        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();


        verify(productRepository,times(1)).findById(product.getCode());

    }

    @Order(4)
    @Test()
    @DisplayName("Should successfully save Product")
    void shouldSuccessfullySaveProduct() {
        Product product = product();
        when(productRepository.findById(product.getCode())).thenReturn(Mono.just(product));
        when(productRepository.save(product)).thenReturn(Mono.just(product));

        final Mono<Product> result = productService.save(product);
        StepVerifier.create(result)
                .expectNextMatches(c -> StringUtils.equalsIgnoreCase(c.getCode(),product.getCode()) &&
                        Objects.equals(c.getCost(),product.getCost()))
                .verifyComplete();


        verify(productRepository,never()).findById(product.getCode());
        verify(productRepository,times(1)).save(product);

    }

    @Order(4)
    @Test()
    @DisplayName("Should successfully delete Product")
    void shouldSuccessfullyDeleteProduct() {
        Product product = product();
        when(productRepository.deleteById(product.getCode())).thenReturn(Mono.empty());

        final Mono<Void> result = productService.deleteById(product.getCode());
        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();


        verify(productRepository,times(1)).deleteById(product.getCode());

    }

    @Order(5)
    @Test()
    @DisplayName("Should successfully update Product if it exists")
    void shouldSuccessfullyUpdateProductIfItExists() {
        Product product = product();
        when(productRepository.findById(product.getCode())).thenReturn(Mono.just(product));
        when(productRepository.save(product)).thenReturn(Mono.just(product));

        final Mono<Product> result = productService.update(product);
        StepVerifier.create(result)
                .expectNextMatches(c -> StringUtils.equalsIgnoreCase(c.getCode(),product.getCode()) &&
                        Objects.equals(c.getCost(),product.getCost()))
                .verifyComplete();


        verify(productRepository,times(1)).findById(product.getCode());
        verify(productRepository,times(1)).save(product);

    }

    @Order(6)
    @Test()
    @DisplayName("Should do nothing if update Product does not exist")
    void shouldDoNothingIfUpdateProductDoesNotExist() {
        Product product = product();
        when(productRepository.findById(product.getCode())).thenReturn(Mono.empty());

        final Mono<Product> result = productService.getProductByCode(product.getCode());
        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();


        verify(productRepository,times(1)).findById(product.getCode());

    }


}
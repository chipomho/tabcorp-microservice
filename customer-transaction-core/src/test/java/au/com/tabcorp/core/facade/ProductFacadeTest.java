package au.com.tabcorp.core.facade;

import au.com.tabcorp.common.model.CustomerModel;
import au.com.tabcorp.common.model.ProductModel;
import au.com.tabcorp.data.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@DisplayName("Product Facade")
class ProductFacadeTest extends AbstractFacadeTest{

    @Autowired()
    protected ProductFacade productFacade;

    protected boolean checkProduct(final ProductModel product, final ProductModel model) {
        return Objects.equals(product.getCode(),model.getCode())
                && Objects.equals(product.getCost(), model.getCost()) && Objects.equals(product.getStatus(), model.getStatus());

    }

    @Order(1)
    @Test()
    @DisplayName("Should successfully load Application Context")
    void shouldSuccessfullyLoadApplicationContext() {
        assertNotNull(productFacade,"ProductFacade is null");
    }

    @Order(2)
    @Test()
    @DisplayName("Should successfully return Product instance if it exists")
    void shouldSuccessfullyReturnCustomerInstanceIfItExists() {
        final ProductModel model = productModel();
        when(productService.getProductByCode(model.getCode())).thenReturn(Mono.just(product(model)));
        final String key = ProductFacade.productKey(model.getCode());
        when(cacheService.get(key, ProductModel.class)).thenReturn(Mono.empty());
        when(cacheService.set(any(String.class),any(ProductModel.class) )).thenReturn(Mono.just(Boolean.TRUE));

        final Mono<ProductModel> result = productFacade.product(model.getCode());
        StepVerifier.create(result)
                .expectNextMatches(product -> checkProduct(product, model) )
                .verifyComplete();

        verify(productService,atMostOnce()).getProductByCode(model.getCode());
        verify(cacheService,atMostOnce()).get(key,ProductModel.class);
        verify(cacheService,atMostOnce()).set(any(String.class),any(ProductModel.class));

    }

    @Order(3)
    @Test()
    @DisplayName("Should return NULL Product instance if product code does not exists")
    void shouldReturnNullProductInstanceIfProductCodeDoesNotExists() {
        final ProductModel model = productModel();
        when(productService.getProductByCode(model.getCode())).thenReturn(Mono.empty());
        final String key = ProductFacade.productKey(model.getCode());
        when(cacheService.get(key, ProductModel.class)).thenReturn(Mono.empty());
        when(cacheService.set(any(String.class),any(ProductModel.class) )).thenReturn(Mono.just(Boolean.TRUE));

        final Mono<ProductModel> result = productFacade.product(model.getCode());
        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();

        verify(productService,atMostOnce()).getProductByCode(model.getCode());
        verify(cacheService,atMostOnce()).get(key,ProductModel.class);
        verify(cacheService,atMostOnce()).set(any(String.class),any(ProductModel.class));

    }

    @Order(4)
    @Test()
    @DisplayName("Should successfully create Product instance")
    void shouldSuccessfullyCreateProductInstance() {
        final ProductModel model = productModel();
        final Product entity = product(model);
        when(productService.save(entity)).thenReturn(Mono.just(entity));
        final String key = ProductFacade.productKey(model.getCode());
        when(cacheService.get(key, ProductModel.class)).thenReturn(Mono.empty());
        when(cacheService.set(any(String.class),any(ProductModel.class) )).thenReturn(Mono.just(Boolean.TRUE));

        final Mono<ProductModel> result = productFacade.createProduct(model);
        StepVerifier.create(result)
                .expectNextMatches(product -> checkProduct(product, model))
                .verifyComplete();

        verify(productService,atMostOnce()).save(entity);
        verify(cacheService,atMostOnce()).get(key,ProductModel.class);
        verify(cacheService,atMostOnce()).set(any(String.class),any(ProductModel.class));

    }

    @Order(5)
    @Test()
    @DisplayName("Should successfully update Product instance")
    void shouldSuccessfullyUpdateProductInstance() {
        final ProductModel model = productModel();
        final Product entity = product(model);
        when(productService.update(entity)).thenReturn(Mono.just(entity));
        final String key = ProductFacade.productKey(model.getCode());
        when(cacheService.get(key, ProductModel.class)).thenReturn(Mono.empty());
        when(cacheService.set(any(String.class),any(ProductModel.class) )).thenReturn(Mono.just(Boolean.TRUE));

        final Mono<ProductModel> result = productFacade.updateProduct(model);
        StepVerifier.create(result)
                .expectNextMatches(product -> checkProduct(product, model))
                .verifyComplete();

        verify(productService,atMostOnce()).update(entity);
        verify(cacheService,atMostOnce()).get(key,ProductModel.class);
        verify(cacheService,atMostOnce()).set(any(String.class),any(ProductModel.class));

    }

    @Order(6)
    @Test()
    @DisplayName("Should successfully delete Customer instance")
    void shouldSuccessfullyDeleteCustomerInstance() {
        final String code = "PRODUCT_001";
        when(productService.deleteById(code)).thenReturn(Mono.empty());

        final Mono<Void> result = productFacade.deleteProduct(code);
        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();

        verify(productService,atMostOnce()).deleteById(code);
    }

}
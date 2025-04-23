package au.com.tabcorp.toolkit.api.controllers;

import au.com.tabcorp.common.enums.ProductStatus;
import au.com.tabcorp.common.model.ProductModel;
import au.com.tabcorp.toolkit.api.utils.WebPathMappings;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import static org.apache.commons.lang3.StringUtils.trim;
import static org.mockito.Mockito.*;

@DisplayName("Product Controller")
class ProductControllerTest extends AbstractControllerTest {

    @Order(1)
    @Test()
    @DisplayName("Should return empty when Product is not found")
    public void shouldReturnEmptyWhenProductIsNotFound() {
        final String code = "PRODUCT_001";
        Mockito.when(productFacade.product(code)).thenReturn(Mono.empty());

        webTestClient.get().uri(String.format("%s/%s", WebPathMappings.CONTROLLER_PRODUCT, trim(code) ))
                .header(HttpHeaders.AUTHORIZATION, token)
                .exchange()
                .expectStatus().isNotFound();

        verify(productFacade,times(1)).product(code);
    }

    @Order(2)
    @Test()
    @DisplayName("Should successfully return Product when record exists")
    public void shouldSuccessfullyReturnProductWhenRecordExists() {
        final String code = "PRODUCT_001";
        Mockito.when(productFacade.product(code)).thenReturn(Mono.just(productModel()));

        webTestClient.get().uri(String.format("%s/%s", WebPathMappings.CONTROLLER_PRODUCT, trim(code) ))
                .header(HttpHeaders.AUTHORIZATION, token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(code)
                .jsonPath("$.status").isEqualTo(ProductStatus.ACTIVE.name());

        verify(productFacade,times(1)).product(code);
    }


    @Order(3)
    @Test()
    @DisplayName("Should successfully Create Product record")
    public void shouldSuccessfullyCreateProductRecord() {
        final ProductModel model = productModel();
        when(productFacade.createProduct(model)).thenReturn(Mono.just(model));
        when(productFacade.product(model.getCode())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri(WebPathMappings.CONTROLLER_PRODUCT)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(model), ProductModel.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.code").isEqualTo(model.getCode())
                .jsonPath("$.status").isEqualTo(ProductStatus.ACTIVE.name());

        verify(productFacade,times(1)).product(model.getCode());
        verify(productFacade,times(1)).createProduct(model);
    }

    @Order(4)
    @Test()
    @DisplayName("Should successfully Update Product record")
    public void shouldSuccessfullyUpdateCustomerRecord() {
        final ProductModel model = productModel();
        Mockito.when(productFacade.updateProduct(model)).thenReturn(Mono.just(model));
        when(productFacade.product(model.getCode())).thenReturn(Mono.just(model));

        webTestClient.put()
                .uri(WebPathMappings.CONTROLLER_PRODUCT)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(model), ProductModel.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(model.getCode())
                .jsonPath("$.status").isEqualTo(ProductStatus.ACTIVE.name());

        verify(productFacade,times(1)).updateProduct(model);
        verify(productFacade,times(1)).product(model.getCode());
    }

    @Order(5)
    @Test()
    @DisplayName("Should successfully Delete Product record")
    public void shouldSuccessfullyDeleteProductRecord() {
        final String code = "PRODUCT_001";
        Mockito.when(productFacade.deleteProduct(code)).thenReturn(Mono.empty());

        webTestClient.delete().uri(String.format("%s/%s", WebPathMappings.CONTROLLER_PRODUCT, trim(code) ))
                .header(HttpHeaders.AUTHORIZATION, token)
                .exchange()
                .expectStatus().isOk();

        verify(productFacade,times(1)).deleteProduct(code);
    }

    @Order(6)
    @Test()
    @DisplayName("Should return Bad Request when Create Product record fails")
    public void shouldReturnBadRequestWhenCreateProductRecordFails() {
        final ProductModel model = productModel();
        model.setCode(null);
        Mockito.when(productFacade.createProduct(model)).thenReturn(Mono.just(model));
        when(productFacade.product(model.getCode())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri(WebPathMappings.CONTROLLER_PRODUCT)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(model), ProductModel.class)
                .exchange()
                .expectStatus().isBadRequest();

        verify(productFacade,never()).createProduct(model);
        verify(productFacade,never()).product(model.getCode());
    }

    @Order(4)
    @Test()
    @DisplayName("Should return Bad Request when Update Product record fails")
    public void shouldReturnBadRequestWhenUpdateCustomerRecordFails() {
        final ProductModel model = productModel();
        Mockito.when(productFacade.createProduct(model)).thenReturn(Mono.just(model));
        when(productFacade.product(model.getCode())).thenReturn(Mono.just(model));

        webTestClient.post()
                .uri(WebPathMappings.CONTROLLER_PRODUCT)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(model), ProductModel.class)
                .exchange()
                .expectStatus().isBadRequest();

        verify(productFacade,never()).createProduct(model);
        verify(productFacade,times(1)).product(model.getCode());
    }


}
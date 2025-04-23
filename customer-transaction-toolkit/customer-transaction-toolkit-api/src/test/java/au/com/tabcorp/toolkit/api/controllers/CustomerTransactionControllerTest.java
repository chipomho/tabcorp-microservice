package au.com.tabcorp.toolkit.api.controllers;

import au.com.tabcorp.common.model.CustomerModel;
import au.com.tabcorp.common.model.ProductModel;
import au.com.tabcorp.common.model.TransactionModel;
import au.com.tabcorp.toolkit.api.utils.WebPathMappings;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@DisplayName("Customer Transaction Controller")
class CustomerTransactionControllerTest extends AbstractControllerTest{


    @Order(2)
    @Test()
    @DisplayName("Should return Bad Request if it fails to add Transaction Model")
    void shouldReturnBadRequestIfItFailsToAddTransactionModel() {

        final TransactionModel model = transactionModel();
        final ProductModel product = productModel();
        final CustomerModel customer = customerModel();

        model.setProductCode(product.getCode());
        model.setCustomerId(customer.getId());
        model.setTransactionTime(LocalDateTime.now().minusDays(1));

        when(customerFacade.customer(model.getCustomerId())).thenReturn(Mono.just(customer));
        when(productFacade.product(product.getCode())).thenReturn(Mono.just(product));

        webTestClient.post()
                .uri(WebPathMappings.CONTROLLER_TRANSACTION)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(model), TransactionModel.class)
                .exchange()
                .expectStatus().isBadRequest();

        verify(productFacade, never()).product(product.getCode());
        verify(customerFacade, never()).customer(model.getCustomerId());
    }

    @Order(3)
    @Test()
    @DisplayName("Should successfully add transaction")
    void shouldSuccessfullyAddTransaction() {

        final TransactionModel model = transactionModel();
        final ProductModel product = productModel();
        final CustomerModel customer = customerModel();

        model.setProductCode(product.getCode());
        model.setCustomerId(customer.getId());
        model.setQuantity(1);

        when(customerFacade.customer(model.getCustomerId())).thenReturn(Mono.just(customer));
        when(productFacade.product(product.getCode())).thenReturn(Mono.just(product));
        when(transactionFacade.createTransaction(model)).thenReturn(Mono.empty());

        webTestClient.post()
                .uri(WebPathMappings.CONTROLLER_TRANSACTION)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(model), TransactionModel.class)
                .exchange()
                .expectStatus().isOk();

        verify(productFacade, times(1)).product(product.getCode());
        verify(customerFacade, times(1)).customer(model.getCustomerId());
        verify(transactionFacade, times(1)).createTransaction(model);
    }

}
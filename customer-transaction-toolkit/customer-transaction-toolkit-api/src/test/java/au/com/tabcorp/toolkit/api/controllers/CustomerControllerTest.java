package au.com.tabcorp.toolkit.api.controllers;

import au.com.tabcorp.common.model.CustomerModel;
import au.com.tabcorp.toolkit.api.utils.WebPathMappings;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@DisplayName("Customer Controller")
class CustomerControllerTest extends AbstractControllerTest{



    @Order(1)
    @Test()
    @DisplayName("Should return empty when customer is not found")
    public void shouldReturnEmptyWhenCustomerIsNotFound() {
        Mockito.when(customerFacade.customer(2L)).thenReturn(Mono.empty());

        webTestClient.get().uri(String.format("%s/2",WebPathMappings.CONTROLLER_CUSTOMER))
                .header(HttpHeaders.AUTHORIZATION, token)
                .exchange()
                .expectStatus().isNotFound();

        verify(customerFacade,times(1)).customer(2L);
    }

    @Order(2)
    @Test()
    @DisplayName("Should successfully return Customer when record exists")
    public void shouldSuccessfullyReturnCustomerWhenRecordExists() {
        final CustomerModel model = customerModel();
        Mockito.when(customerFacade.customer(2L)).thenReturn(Mono.just(model));

        webTestClient.get().uri(String.format("%s/2",WebPathMappings.CONTROLLER_CUSTOMER))
                .header(HttpHeaders.AUTHORIZATION, token)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.firstName").isEqualTo(model.getFirstName())
                .jsonPath("$.emailAddress").isEqualTo(model.getEmailAddress());;

        verify(customerFacade,times(1)).customer(2L);
    }


    @Order(3)
    @Test()
    @DisplayName("Should successfully Create Customer record")
    public void shouldSuccessfullyCreateCustomerRecord() {
        final CustomerModel model = customerModel();
        Mockito.when(customerFacade.createCustomer(model)).thenReturn(Mono.just(model));

        webTestClient.post()
                .uri(WebPathMappings.CONTROLLER_CUSTOMER)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(model), CustomerModel.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(model.getId())
                .jsonPath("$.firstName").isEqualTo(model.getFirstName());
        verify(customerFacade,times(1)).createCustomer(model);
    }

    @Order(4)
    @Test()
    @DisplayName("Should successfully Update Customer record")
    public void shouldSuccessfullyUpdateCustomerRecord() {
        final CustomerModel model = customerModel();
        Mockito.when(customerFacade.updateCustomer(model)).thenReturn(Mono.just(model));
        Mockito.when(customerFacade.customer(model.getId())).thenReturn(Mono.just(model));

        webTestClient.put()
                .uri(WebPathMappings.CONTROLLER_CUSTOMER)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(model), CustomerModel.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(model.getId())
                .jsonPath("$.firstName").isEqualTo(model.getFirstName());
        verify(customerFacade,times(1)).updateCustomer(model);
        verify(customerFacade,times(1)).customer(model.getId());
    }

    @Order(5)
    @Test()
    @DisplayName("Should successfully Delete Customer record")
    public void shouldSuccessfullyDeleteCustomerRecord() {
        Mockito.when(customerFacade.deleteCustomer(2L)).thenReturn(Mono.empty());

        webTestClient.delete().uri(String.format("%s/2",WebPathMappings.CONTROLLER_CUSTOMER))
                .header(HttpHeaders.AUTHORIZATION, token)
                .exchange()
                .expectStatus().isOk();

        verify(customerFacade,times(1)).deleteCustomer(2L);
    }

    @Order(6)
    @Test()
    @DisplayName("Should return Bad Request when Create Customer record fails")
    public void shouldReturnBadRequestWhenCreateCustomerRecordFails() {
        final CustomerModel model = customerModel();
        model.setEmailAddress(null);
        Mockito.when(customerFacade.createCustomer(model)).thenReturn(Mono.empty());
        Mockito.when(customerFacade.customer(model.getId())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri(WebPathMappings.CONTROLLER_CUSTOMER)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(model), CustomerModel.class)
                .exchange()
                .expectStatus().isBadRequest();
        verify(customerFacade,never()).createCustomer(model);
        verify(customerFacade,never()).customer(model.getId());
    }

    @Order(4)
    @Test()
    @DisplayName("Should return Bad Request when Update Customer record fails")
    public void shouldReturnBadRequestWhenUpdateCustomerRecordFails() {
        final CustomerModel model = customerModel();
        Mockito.when(customerFacade.updateCustomer(model)).thenReturn(Mono.empty());
        Mockito.when(customerFacade.customer(model.getId())).thenReturn(Mono.empty());
        webTestClient.put()
                .uri(WebPathMappings.CONTROLLER_CUSTOMER)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(model), CustomerModel.class)
                .exchange()
                .expectStatus().isBadRequest();
        verify(customerFacade,never()).updateCustomer(model);
        verify(customerFacade,times(1)).customer(model.getId());
    }

}
package au.com.tabcorp.toolkit.api.controllers;

import au.com.tabcorp.common.model.CustomerTransactionCostsModel;
import au.com.tabcorp.common.model.LocationTransactionNumberModel;
import au.com.tabcorp.common.model.ProductTransactionCostsModel;
import au.com.tabcorp.toolkit.api.utils.WebPathMappings;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Reporting Controller")
class ReportingControllerTest extends AbstractControllerTest {

    @Order(1)
    @Test()
    @DisplayName("Should successfully return Customer Transaction Costs Model for specific customer")
    public void shouldSuccessfullyReturnCustomerTransactionCostsModelForSpecificCustomer() {
        final Long customerId = 1L;
        final CustomerTransactionCostsModel model = customerTransactionCostsModel();
        Mockito.when(reportingFacade.costOfTransactionPerCustomer(customerId)).thenReturn(Mono.just(model));

        webTestClient.get().uri(String.format("%s/customer/%d",WebPathMappings.CONTROLLER_REPORT, customerId))
                .header(HttpHeaders.AUTHORIZATION, token)
                .exchange()
                .expectStatus().isOk().expectBody()
                .jsonPath("$.costs").isEqualTo(model.getCosts())
                .jsonPath("$.firstName").isEqualTo(model.getFirstName());


        verify(reportingFacade,times(1)).costOfTransactionPerCustomer(customerId);
    }

    @Order(2)
    @Test()
    @DisplayName("Should successfully return Customer Transaction Costs Model for all customers")
    public void shouldSuccessfullyReturnCustomerTransactionCostsModelForAllCustomers() {
        final CustomerTransactionCostsModel[] model = new CustomerTransactionCostsModel[]{ customerTransactionCostsModel() };
        Mockito.when(reportingFacade.costOfTransactionsPerCustomer()).thenReturn(Flux.just(model));

        webTestClient.get().uri(WebPathMappings.CONTROLLER_REPORT+"/customers")
                .header(HttpHeaders.AUTHORIZATION, token)
                .exchange()
                .expectStatus().isOk();


        verify(reportingFacade,times(1)).costOfTransactionsPerCustomer();
    }

    @Order(3)
    @Test()
    @DisplayName("Should successfully return Product Transaction Costs Model for specific product")
    public void shouldSuccessfullyReturnCus() {
        final String code ="PRODUCT_001";
        final ProductTransactionCostsModel model = productTransactionCostsModel();
        Mockito.when(reportingFacade.costOfTransactionPerProduct(code)).thenReturn(Mono.just(model));

        webTestClient.get().uri(String.format("%s/product/%s",WebPathMappings.CONTROLLER_REPORT, code))
                .header(HttpHeaders.AUTHORIZATION, token)
                .exchange()
                .expectStatus().isOk().expectBody()
                .jsonPath("$.costs").isEqualTo(model.getCosts())
                .jsonPath("$.code").isEqualTo(model.getCode());


        verify(reportingFacade,times(1)).costOfTransactionPerProduct(code);
    }

    @Order(4)
    @Test()
    @DisplayName("Should successfully return Product Transaction Costs Model for all products")
    public void shouldSuccessfullyReturnProductTransactionCostsModelForAllProducts() {
        final ProductTransactionCostsModel[] model = new ProductTransactionCostsModel[]{ productTransactionCostsModel() };
        Mockito.when(reportingFacade.costOfTransactionsPerProduct()).thenReturn(Flux.just(model));

        webTestClient.get().uri(WebPathMappings.CONTROLLER_REPORT+"/products")
                .header(HttpHeaders.AUTHORIZATION, token)
                .exchange()
                .expectStatus().isOk().expectBody();


        verify(reportingFacade,times(1)).costOfTransactionsPerProduct();
    }

    @Order(4)
    @Test()
    @DisplayName("Should successfully return Product Transaction Count for Australian Customers")
    public void shouldSuccessfullyReturnProductTransactionCountForAustralianCustomers() {
        final LocationTransactionNumberModel model = LocationTransactionNumberModel.builder().location("Australia")
                .transactions(100L).build();
        Mockito.when(reportingFacade.numberOfTransactionsForLocation(model.getLocation())).thenReturn(Mono.just(model));

        webTestClient.get().uri(WebPathMappings.CONTROLLER_REPORT+"/australia")
                .header(HttpHeaders.AUTHORIZATION, token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.location").isEqualTo(model.getLocation())
                .jsonPath("$.transactions").isEqualTo(model.getTransactions());


        verify(reportingFacade,times(1)).numberOfTransactionsForLocation(model.getLocation());
    }

}
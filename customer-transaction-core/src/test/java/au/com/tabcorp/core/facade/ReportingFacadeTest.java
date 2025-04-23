package au.com.tabcorp.core.facade;

import au.com.tabcorp.common.model.CustomerTransactionCostsModel;
import au.com.tabcorp.common.model.ProductTransactionCostsModel;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@DisplayName("Reporting Facade")
class ReportingFacadeTest extends AbstractFacadeTest {

    @Autowired()
    protected ReportingFacade reportingFacade;

    @Order(1)
    @Test()
    @DisplayName("Should successfully load Application Context")
    void shouldSuccessfullyLoadApplicationContext() {
        assertNotNull(reportingFacade,"Reporting Facade is null");
    }

    @Order(2)
    @Test()
    @DisplayName("Should successfully return cost of Transaction Per Customer")
    void shouldSuccessfullyReturnCostOfTransactionPerCustomer() {
        final Long customerId = 1L;
        final CustomerTransactionCostsModel model = customerTransactionCostsModel();
        when(customerTransactionService.costOfTransactionPerCustomer(customerId)).thenReturn(Mono.just(model));

        final Mono<CustomerTransactionCostsModel> result = reportingFacade.costOfTransactionPerCustomer(customerId);
        StepVerifier.create(result)
                .expectNextMatches(report -> Objects.equals(report.getId(), model.getId())
                        && StringUtils.equalsIgnoreCase(report.getFirstName(), model.getFirstName()))
                .verifyComplete();

        verify(customerTransactionService,atMostOnce()).costOfTransactionPerCustomer(customerId);
    }

    @Order(2)
    @Test()
    @DisplayName("Should successfully return cost of Transactions for all Customers")
    void shouldSuccessfullyReturnCostOfTransactionsForAllCustomers() {
        final Long customerId = 1L;
        final CustomerTransactionCostsModel[] model = new CustomerTransactionCostsModel[]{ customerTransactionCostsModel() };
        when(customerTransactionService.costOfTransactionPerCustomer()).thenReturn(Flux.just(model));

        final Flux<CustomerTransactionCostsModel> result = reportingFacade.costOfTransactionsPerCustomer();
        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();

        verify(customerTransactionService,atMostOnce()).costOfTransactionPerCustomer(customerId);
    }

    @Order(3)
    @Test()
    @DisplayName("Should successfully return cost of Transaction Per Product")
    void shouldSuccessfullyRet() {
        final ProductTransactionCostsModel model = productTransactionCostsModel();
        when(customerTransactionService.costOfTransactionPerProduct(model.getCode())).thenReturn(Mono.just(model));

        final Mono<ProductTransactionCostsModel> result = reportingFacade.costOfTransactionPerProduct(model.getCode());
        StepVerifier.create(result)
                .expectNextMatches(report -> Objects.equals(report.getCosts(), model.getCosts())
                        && StringUtils.equalsIgnoreCase(report.getCode(), model.getCode()))
                .verifyComplete();

        verify(customerTransactionService,atMostOnce()).costOfTransactionPerProduct(model.getCode());
    }

    @Order(4)
    @Test()
    @DisplayName("Should successfully return cost of Transactions for all products")
    void shouldSuccessfullyReturnCostOfTransactionsForAllProducts() {
        final ProductTransactionCostsModel[] model =
                new ProductTransactionCostsModel []{productTransactionCostsModel()};
        when(customerTransactionService.costOfTransactionsPerProduct()).thenReturn(Flux.just(model));

        final Flux<ProductTransactionCostsModel> result = reportingFacade.costOfTransactionsPerProduct();
        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();

        verify(customerTransactionService,atMostOnce()).costOfTransactionsPerProduct();
    }

}
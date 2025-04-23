package au.com.tabcorp.data.service;

import au.com.tabcorp.common.model.CustomerTransactionCostsModel;
import au.com.tabcorp.common.model.ProductTransactionCostsModel;
import au.com.tabcorp.data.entity.CustomerTransaction;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Objects;

import static org.mockito.Mockito.*;

@DisplayName("Customer Transaction Service")
class CustomerTransactionServiceTest extends AbstractServiceTest{

    @Autowired()
    private CustomerTransactionService customerTransactionService;

    @Order(1)
    @Test()
    @DisplayName("Should successfully load Application Context")
    void shouldSuccessfullyLoadApplicationContext() {
        Assertions.assertNotNull(customerTransactionService,"Customer Transaction Service is NULL");
    }

    @Order(2)
    @Test()
    @DisplayName("Should successfully save Customer Transaction entity")
    void shouldSuccessfullySaveCustomerTransactionEntity() {
        final CustomerTransaction customerTransaction = customerTransaction();
        when(customerTransactionRepository.save(customerTransaction)).thenReturn(Mono.just(customerTransaction));

        final Mono<CustomerTransaction> entity = customerTransactionService.save(customerTransaction);

        StepVerifier.create(entity)
                .expectNextMatches(c -> StringUtils.equalsIgnoreCase(c.getProductCode(),customerTransaction.getProductCode()) &&
                        Objects.equals(c.getCustomerId(),customerTransaction.getCustomerId()))
                .verifyComplete();

        verify(customerTransactionRepository,times(1)).save(customerTransaction);

    }

    @Order(3)
    @Test()
    @DisplayName("Should successfully return Customer Transaction Costs by customer id")
    void shouldSuccessfullyReturnCustomerTransactionCostsByCustomerId() {
        final Long customerId = 20L;
        CustomerTransactionCostsModel costsModel = CustomerTransactionCostsModel.builder()
                .costs(new BigDecimal("2000.00")).firstName("Tony").lastName("Stark").id(customerId).build();
        when(customerTransactionRepository.costOfTransactionPerCustomer(customerId)).thenReturn(Mono.just(costsModel));

        final Mono<CustomerTransactionCostsModel> model = customerTransactionService.costOfTransactionPerCustomer(customerId);

        StepVerifier.create(model)
                .expectNextMatches(c -> StringUtils.equalsIgnoreCase(c.getFirstName(),costsModel.getFirstName()) &&
                        Objects.equals(c.getCosts(),costsModel.getCosts()))
                .verifyComplete();

        verify(customerTransactionRepository,times(1)).costOfTransactionPerCustomer(customerId);

    }

    @Order(4)
    @Test()
    @DisplayName("Should successfully return Customer Transaction Costs for all customers")
    void shouldSuccessfullyReturnCustomerTransactionCostsForAllCustomers() {
        CustomerTransactionCostsModel[] costsModel =new CustomerTransactionCostsModel[]{ CustomerTransactionCostsModel.builder()
                .costs(new BigDecimal("2000.00")).firstName("Tony").lastName("Stark").id(100L).build(),
                CustomerTransactionCostsModel.builder()
                        .costs(new BigDecimal("100.00")).firstName("May").lastName("Jeanine").id(101L).build(),
                CustomerTransactionCostsModel.builder()
                        .costs(new BigDecimal("20.00")).firstName("Farai").lastName("Mombe").id(102L).build()};
        when(customerTransactionRepository.costOfTransactionPerCustomer()).thenReturn(Flux.just(costsModel));

        final Flux<CustomerTransactionCostsModel> model = customerTransactionService.costOfTransactionPerCustomer();

        StepVerifier.create(model)
                .expectNextCount(costsModel.length)
                .verifyComplete();

        verify(customerTransactionRepository,times(1)).costOfTransactionPerCustomer();

    }

    @Order(5)
    @Test()
    @DisplayName("Should successfully return Customer Transaction Costs by product code")
    void shouldSuccessfullyReturnCustomerTransactionCostsByProductCode() {
        final String code = "PRODUCT_001";
        ProductTransactionCostsModel costsModel = ProductTransactionCostsModel.builder()
                .costs(new BigDecimal("2000.00")).code(code).build();
        when(customerTransactionRepository.costOfTransactionPerProduct(code)).thenReturn(Mono.just(costsModel));

        final Mono<ProductTransactionCostsModel> model = customerTransactionService.costOfTransactionPerProduct(code);

        StepVerifier.create(model)
                .expectNextMatches(c -> StringUtils.equalsIgnoreCase(c.getCode(),costsModel.getCode()) &&
                        Objects.equals(c.getCosts(),costsModel.getCosts()))
                .verifyComplete();

        verify(customerTransactionRepository,times(1)).costOfTransactionPerProduct(code);

    }

    @Order(6)
    @Test()
    @DisplayName("Should successfully return Customer Transaction Costs for all products")
    void shouldSuccessfullyReturnCustomerTransactionCostsForAllProducts() {
        ProductTransactionCostsModel[] costsModel = new ProductTransactionCostsModel[]{ ProductTransactionCostsModel.builder()
                .costs(new BigDecimal("2000.00")).code("PRODUCT_001").build()};
        when(customerTransactionRepository.costOfTransactionsPerProduct()).thenReturn(Flux.just(costsModel));

        final Flux<ProductTransactionCostsModel> model = customerTransactionService.costOfTransactionsPerProduct();

        StepVerifier.create(model)
                .expectNextCount(1)
                .verifyComplete();

        verify(customerTransactionRepository,times(1)).costOfTransactionsPerProduct();


    }

}
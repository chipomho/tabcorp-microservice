package au.com.tabcorp.core.facade;

import au.com.tabcorp.common.model.TransactionModel;
import au.com.tabcorp.data.entity.CustomerTransaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@DisplayName("Transaction Facade")
class TransactionFacadeTest extends AbstractFacadeTest{

    @Autowired()
    protected TransactionFacade transactionFacade;

    @Order(1)
    @Test()
    @DisplayName("Should successfully load Application Context")
    void shouldSuccessfullyLoadApplicationContext() {
        assertNotNull(transactionFacade,"Transaction Facade is null");
    }

    @Order(2)
    @Test()
    @DisplayName("Should successfully create Customer Transaction instance")
    void shouldSuccessfullyCreateCustomerTransactionInstance() {
        final TransactionModel model = transactionModel();
        final CustomerTransaction entity = customerTransaction(model);
        when(customerTransactionService.save(entity)).thenReturn(Mono.just(entity));

        final Mono<Void> result = transactionFacade.createTransaction(model);
        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();

        verify(customerTransactionService,atMostOnce()).save(entity);
    }



}
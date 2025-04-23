package au.com.tabcorp.core.facade;

import au.com.tabcorp.common.BeanNames;
import au.com.tabcorp.common.model.TransactionModel;
import au.com.tabcorp.core.annotations.Facade;
import au.com.tabcorp.data.entity.CustomerTransaction;
import au.com.tabcorp.data.service.CustomerTransactionService;
import au.com.tabcorp.messaging.service.MessageSender;
import org.omg.IOP.TransactionService;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Mono;


/**
 * Exposes models only and hides away all database entities. Also serves to do other
 * orchestration needed at the core layer, for example interact with the messaging layer.
 * <code>TransactionFacade</code>
 */
@Facade(BeanNames.Core.TRANSACTION_FACADE)
public class TransactionFacade extends AbstractFacade {

    protected final CustomerTransactionService customerTransactionService;
    protected final MessageSender messageSender;

    @Value("${tabcorp.app.messaging-mode:false}")
    protected boolean messagingMode = false;

    public TransactionFacade(final CustomerTransactionService customerTransactionService, final MessageSender messageSender) {
        this.customerTransactionService = customerTransactionService;
        this.messageSender = messageSender;
    }
    /**
     * Simply add the Transaction into the database.
     * @param transactionData Transaction data to save to the database
     * @return simply return the id back to the client, no need to return the whole entity
     */
    public Mono<Void> createTransaction(final TransactionModel transactionData) {
      return messagingMode ? messageSender.sendMessage(transactionData) :
       customerTransactionService.save(CustomerTransaction.builder().transactionTime(transactionData.getTransactionTime())
              .customerId(transactionData.getCustomerId()).productCode(transactionData.getProductCode())
              .quantity(transactionData.getQuantity()).build()).then();
    };

}

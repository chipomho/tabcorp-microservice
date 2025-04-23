package au.com.tabcorp.toolkit.api.delegates;

import au.com.tabcorp.common.BeanNames;
import au.com.tabcorp.common.model.CustomerModel;
import au.com.tabcorp.common.model.TransactionModel;
import au.com.tabcorp.core.annotations.Delegate;
import au.com.tabcorp.core.facade.CustomerFacade;
import au.com.tabcorp.core.facade.TransactionFacade;
import au.com.tabcorp.toolkit.api.validators.TransactionModelValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import reactor.core.publisher.Mono;

@Delegate(BeanNames.ToolkitAPI.DELEGATE_CUSTOMER_TRANSACTION)
public class CustomerTransactionControllerDelegate extends AbstractDelegate{

    protected final CustomerFacade customerFacade;
    protected final TransactionFacade transactionFacade;
    protected final TransactionModelValidator transactionModelValidator;

    public CustomerTransactionControllerDelegate(@Autowired() @Qualifier(BeanNames.Core.CUSTOMER_FACADE) final CustomerFacade customerFacade,
                                                 @Autowired() @Qualifier(BeanNames.Core.TRANSACTION_FACADE) final TransactionFacade transactionFacade,
                                                 @Autowired() @Qualifier(BeanNames.ToolkitAPI.VALIDATOR_TRANSACTION_MODEL) final TransactionModelValidator transactionModelValidator) {
        this.customerFacade = customerFacade;
        this.transactionModelValidator = transactionModelValidator;
        this.transactionFacade = transactionFacade;
    }

    public Mono<CustomerModel> getCustomerById(final Long id){
      return customerFacade.customer(id);
    }

    public Mono<BindingResult> validate(final TransactionModel model) {
        return transactionModelValidator.validate(model);
    }

    public Mono<Void> createTransaction(final TransactionModel data) {
        return transactionFacade.createTransaction( data );
    }
}

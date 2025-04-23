package au.com.tabcorp.data.service;

import au.com.tabcorp.common.BeanNames;
import au.com.tabcorp.common.model.CustomerTransactionCostsModel;
import au.com.tabcorp.common.model.LocationTransactionNumberModel;
import au.com.tabcorp.common.model.ProductTransactionCostsModel;
import au.com.tabcorp.common.model.TransactionModel;
import au.com.tabcorp.data.entity.CustomerTransaction;
import au.com.tabcorp.data.repository.CustomerTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service(BeanNames.Data.SERVICE_CUSTOMER_TRANSACTION)
public class CustomerTransactionService {

    /**
     *  Customer Transaction Repository Instance
     */
    private final CustomerTransactionRepository customerTransactionRepository;

    /**
     * Creates a new <code>CustomerTransactionService</code> instance and also initialises the required
     * repositories.
     *
     * @param customerTransactionRepository <code>CustomerTransactionRepository</code> bean instance
     */
    public CustomerTransactionService(@Autowired()@Qualifier(BeanNames.Data.REPOSITORY_CUSTOMER_TRANSACTION)final CustomerTransactionRepository customerTransactionRepository) {
        this.customerTransactionRepository = customerTransactionRepository;
    }


    /**
     * Saves a new <code>CustomerTransaction</code>
     *
     * @param customerTransaction <code>CustomerTransaction</code> instance to save
     * @return the created <code>CustomerTransaction</code>
     */
    public Mono<CustomerTransaction> save(final CustomerTransaction customerTransaction) {
        return customerTransactionRepository.save(customerTransaction);
    }

    public Mono<CustomerTransactionCostsModel> costOfTransactionPerCustomer(Long customerId) {
        return customerTransactionRepository.costOfTransactionPerCustomer(customerId);
    }

    public Flux<CustomerTransactionCostsModel> costOfTransactionPerCustomer() {
        return customerTransactionRepository.costOfTransactionPerCustomer();
    }

    public Flux<ProductTransactionCostsModel> costOfTransactionsPerProduct() {
        return customerTransactionRepository.costOfTransactionsPerProduct();
    }

    public Mono<ProductTransactionCostsModel> costOfTransactionPerProduct(String code) {
        return customerTransactionRepository.costOfTransactionPerProduct(code);
    }

    public Mono<LocationTransactionNumberModel> numberOfTransactionsForLocation(final String location) {
        return customerTransactionRepository.numberOfTransactionsForLocation(location);
    }
}

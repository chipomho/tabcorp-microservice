package au.com.tabcorp.core.facade;

import au.com.tabcorp.common.BeanNames;
import au.com.tabcorp.common.model.CustomerTransactionCostsModel;
import au.com.tabcorp.common.model.LocationTransactionNumberModel;
import au.com.tabcorp.common.model.ProductTransactionCostsModel;
import au.com.tabcorp.core.annotations.Facade;
import au.com.tabcorp.data.service.CustomerTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Exposes models only and hides away all database entities. Also serves to do other
 * orchestration needed at the core layer, for example interact with the messaging layer.
 * <code>ReportingFacade</code>
 */
@Facade(BeanNames.Core.REPORTING_FACADE)
public class ReportingFacade extends AbstractFacade{

    /**
     * <code>CustomerTransactionService</code> Injected when the <code>ReportingFacade</code> bean instance is created.
     */
    protected final CustomerTransactionService customerTransactionService;

    /**
     * Creates a new bean instance of <code>ReportingFacade</code> with all the required services.
     *
     * @param customerTransactionService <code>CustomerTransactionService</code> bean instance
     */
    public ReportingFacade(@Autowired()@Qualifier(BeanNames.Data.SERVICE_CUSTOMER_TRANSACTION) final CustomerTransactionService customerTransactionService) {
        this.customerTransactionService = customerTransactionService;
    }


    public Mono<CustomerTransactionCostsModel> costOfTransactionPerCustomer(final Long customerId){
      return customerTransactionService.costOfTransactionPerCustomer(customerId);
    }

    public Flux<CustomerTransactionCostsModel> costOfTransactionsPerCustomer(){
        return customerTransactionService.costOfTransactionPerCustomer();
    }


    public Mono<ProductTransactionCostsModel> costOfTransactionPerProduct(final String code){
        return customerTransactionService.costOfTransactionPerProduct(code);
    }

    public Flux<ProductTransactionCostsModel> costOfTransactionsPerProduct(){
        return customerTransactionService.costOfTransactionsPerProduct();
    }

    public Mono<LocationTransactionNumberModel> numberOfTransactionsForLocation(final String location){
        return customerTransactionService.numberOfTransactionsForLocation(location);
    }
}

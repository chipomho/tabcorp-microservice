package au.com.tabcorp.toolkit.api.delegates;

import au.com.tabcorp.common.BeanNames;
import au.com.tabcorp.common.model.CustomerTransactionCostsModel;
import au.com.tabcorp.common.model.LocationTransactionNumberModel;
import au.com.tabcorp.common.model.ProductTransactionCostsModel;
import au.com.tabcorp.core.annotations.Delegate;
import au.com.tabcorp.core.facade.ReportingFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Delegate(BeanNames.ToolkitAPI.DELEGATE_REPORTING)
public class ReportingControllerDelegate extends AbstractDelegate{

    protected final ReportingFacade reportingFacade;

    public ReportingControllerDelegate(@Autowired()@Qualifier(BeanNames.Core.REPORTING_FACADE) final ReportingFacade reportingFacade) {
        this.reportingFacade = reportingFacade;
    }

    public Flux<CustomerTransactionCostsModel> costOfTransactionsPerCustomer() {
        return reportingFacade.costOfTransactionsPerCustomer();
    }

    public Mono<CustomerTransactionCostsModel> costOfTransactionPerCustomer(final Long customerId) {
        return reportingFacade.costOfTransactionPerCustomer(customerId);
    }

    public Mono<ProductTransactionCostsModel> costOfTransactionPerProduct(String code) {
        return reportingFacade.costOfTransactionPerProduct(code);
    }

    public Flux<ProductTransactionCostsModel> costOfTransactionsPerProduct() {
        return reportingFacade.costOfTransactionsPerProduct();
    }

    public Mono<LocationTransactionNumberModel> numberOfTransactionsForLocation(final String location) {
        return reportingFacade.numberOfTransactionsForLocation(location);
    }
}

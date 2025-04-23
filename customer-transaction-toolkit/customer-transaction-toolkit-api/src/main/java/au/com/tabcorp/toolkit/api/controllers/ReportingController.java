package au.com.tabcorp.toolkit.api.controllers;

import au.com.tabcorp.common.model.CustomerTransactionCostsModel;
import au.com.tabcorp.common.model.LocationTransactionNumberModel;
import au.com.tabcorp.common.model.ProductTransactionCostsModel;
import au.com.tabcorp.toolkit.api.delegates.ReportingControllerDelegate;
import au.com.tabcorp.toolkit.api.utils.WebPathMappings;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping(WebPathMappings.CONTROLLER_REPORT)
@PreAuthorize("hasAuthority('STANDARD')")
public class ReportingController extends AbstractController<ReportingControllerDelegate> {

    protected ReportingController(final ReportingControllerDelegate delegate) {
        super(delegate);
    }

    @GetMapping("/customer/{customerId}")
    public Mono<CustomerTransactionCostsModel> costOfTransactionPerCustomer(@PathVariable("customerId") final Long customerId){
        return d().costOfTransactionPerCustomer(customerId);
    }

    @GetMapping("/customers")
    public Flux<CustomerTransactionCostsModel> costOfTransactionsPerCustomer(){
        return d().costOfTransactionsPerCustomer();
    }

    @GetMapping("/product/{code}")
    public Mono<ProductTransactionCostsModel> costOfTransactionPerProduct(@PathVariable("code") final String code){
        return d().costOfTransactionPerProduct(code);
    }

    @GetMapping("/products")
    public Flux<ProductTransactionCostsModel> costOfTransactionsPerProduct(){
        return d().costOfTransactionsPerProduct();
    }

    @GetMapping("/australia")
    public Mono<LocationTransactionNumberModel> numberOfTransactionForAustralia(){
        return d().numberOfTransactionsForLocation("Australia");
    }

}

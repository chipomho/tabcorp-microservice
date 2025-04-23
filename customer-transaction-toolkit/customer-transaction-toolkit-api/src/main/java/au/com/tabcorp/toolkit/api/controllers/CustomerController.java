package au.com.tabcorp.toolkit.api.controllers;

import au.com.tabcorp.common.model.CustomerModel;
import au.com.tabcorp.toolkit.api.delegates.CustomerControllerDelegate;
import au.com.tabcorp.toolkit.api.utils.WebPathMappings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping(WebPathMappings.CONTROLLER_CUSTOMER)
@PreAuthorize("hasAuthority('CUSTOMER')")
public class CustomerController extends AbstractController<CustomerControllerDelegate> {

    public CustomerController(final CustomerControllerDelegate customerControllerDelegate) {
        super(customerControllerDelegate);
    }


    @PostMapping()
    public Mono<ResponseEntity<?>> createCustomer(@RequestBody final Mono<CustomerModel> customerData) {
        return customerData.flatMap(customer->
            d().validateCreate(customer)
                    .flatMap(errors->{
                        if (errors.hasErrors()) {
                            return Mono.just(new ResponseEntity<>( wrap(d().build(errors)), HttpStatus.BAD_REQUEST) );
                        }
                        return d().createCustomer(customer)
                                .map(result->new ResponseEntity<>(result,HttpStatus.CREATED)) ;
                    })
        );
    }

    @PutMapping()
    public Mono<ResponseEntity<?>> updateCustomer(@RequestBody final Mono<CustomerModel> customerData) {
        return customerData.flatMap(customer->
                d().validateUpdate(customer)
                        .flatMap(errors->{
                            if (errors.hasErrors()) {
                                return Mono.just(new ResponseEntity<>( wrap(d().build(errors)), HttpStatus.BAD_REQUEST) );
                            }
                            return d().updateCustomer(customer)
                                    .map(result->new ResponseEntity<>(result,HttpStatus.OK)) ;
                        })
        );
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<CustomerModel>> readCustomer(@PathVariable("id") final Long id) {
        return d().getCustomerById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteCustomer(@PathVariable("id") final Long id) {
        return d().deleteCustomer(id);
    }



}

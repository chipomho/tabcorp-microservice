package au.com.tabcorp.toolkit.api.controllers;

import au.com.tabcorp.common.model.TransactionModel;
import au.com.tabcorp.toolkit.api.delegates.CustomerTransactionControllerDelegate;
import au.com.tabcorp.toolkit.api.utils.WebPathMappings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping(WebPathMappings.CONTROLLER_TRANSACTION)
@PreAuthorize("hasAuthority('STANDARD')")
public class CustomerTransactionController extends AbstractController<CustomerTransactionControllerDelegate> {

    public CustomerTransactionController(final CustomerTransactionControllerDelegate delegate) {
        super(delegate);
    }

    /**
     * Creates a new Transaction
     *
     * @param transactionData
     * @return
     */
    @PostMapping()
    public Mono<ResponseEntity<?>> addTransaction(@RequestBody final Mono<TransactionModel> transactionData) {
        return transactionData.flatMap(transaction-> d().validate(transaction)
                .flatMap(errors->{
                    if (errors.hasErrors()) {
                        return Mono.just(new ResponseEntity<>( wrap(d().build(errors))  , HttpStatus.BAD_REQUEST) );
                    }
                    return d().createTransaction(transaction)
                                      .map(result->new ResponseEntity<>(result,HttpStatus.CREATED)) ;
                }) );
    }


}

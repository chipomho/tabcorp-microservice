package au.com.tabcorp.toolkit.api.delegates;

import au.com.tabcorp.common.BeanNames;
import au.com.tabcorp.common.model.CustomerModel;
import au.com.tabcorp.core.annotations.Delegate;
import au.com.tabcorp.core.facade.CustomerFacade;
import au.com.tabcorp.toolkit.api.validators.CustomerModelValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.BindingResult;
import reactor.core.publisher.Mono;

@Delegate(BeanNames.ToolkitAPI.DELEGATE_CUSTOMER)
public class CustomerControllerDelegate extends AbstractDelegate{

    protected final CustomerFacade customerFacade;
    protected final CustomerModelValidator customerModelValidator;

    public CustomerControllerDelegate(@Autowired()@Qualifier(BeanNames.Core.CUSTOMER_FACADE)final CustomerFacade customerFacade,
                                      @Autowired()@Qualifier(BeanNames.ToolkitAPI.VALIDATOR_CUSTOMER_MODEL)final CustomerModelValidator customerModelValidator) {
        this.customerFacade = customerFacade;
        this.customerModelValidator = customerModelValidator;
    }

    public Mono<CustomerModel> getCustomerById(final Long id){
      return customerFacade.customer(id);
    }

    public Mono<BindingResult> validateCreate(final CustomerModel model) {
        return customerModelValidator.validateCreate(model);
    }

    public Mono<BindingResult> validateUpdate(final CustomerModel model) {
        return customerModelValidator.validateUpdate(model);
    }

    public Mono<CustomerModel> createCustomer(final CustomerModel data) {
        return customerFacade.createCustomer( data );
    }

    public Mono<CustomerModel> updateCustomer(final CustomerModel data) {
        return customerFacade.updateCustomer( data );
    }

    public Mono<Void> deleteCustomer(final Long id){
        return customerFacade.deleteCustomer(id);
    }
}

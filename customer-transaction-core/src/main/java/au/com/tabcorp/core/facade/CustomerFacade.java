package au.com.tabcorp.core.facade;

import au.com.tabcorp.common.BeanNames;
import au.com.tabcorp.common.model.CustomerModel;
import au.com.tabcorp.core.annotations.Facade;
import au.com.tabcorp.data.entity.Customer;
import au.com.tabcorp.data.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * Exposes models only and hides away all database entities. Also serves to do other
 * orchestration needed at the core layer, for example interact with the messaging layer.
 * <code>CustomerFacade</code>
 */
@Facade(BeanNames.Core.CUSTOMER_FACADE)
public class CustomerFacade extends AbstractFacade{

    /**
     * <code>CustomerService</code> Injected when the <code>CustomerFacade</code> bean instance is created.
     */
    protected final CustomerService customerService;

    /**
     * Function to cache the CustomerModel
     */
    protected Function<CustomerModel, Mono<CustomerModel>> cacheCustomer = (final CustomerModel customer)
            -> {
        try{
            return cacheService.set(customerId(customer.getId()), customer).thenReturn(customer);
        }
        catch (Exception e) {
            return Mono.error(e);
        }
    };

    /**
     * Standard method to create a Customer Id
     * @param id customer id
     * @return Formatted cache key
     */
    protected static String customerId(final Long id){
        return String.format("Customer:%d",id);
    }

    /**
     * Creates a new bean instance of <code>CustomerService</code> with all the required services.
     *
     * @param customerService <code>CustomerService</code> bean instance
     */
    public CustomerFacade(@Autowired()@Qualifier(BeanNames.Data.SERVICE_CUSTOMER) final CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Returns the <code>CustomerModel</code> a <strong>DTO</strong> for the <code>Customer</code> entity
     * when a search by <code>id</code> is done. A quick cache search is performed first if not found then a database
     * search happens
     *
     * @param id <code>Customer</code> primary key id.
     * @return Mono of the <code>CustomerModel</code> otherwise an empty Mono is returned.
     */
    public Mono<CustomerModel> customer(final Long id) {
        final String key = customerId(id);
        return cacheService.get(key,CustomerModel.class)
                .switchIfEmpty(customerService.customer(id).map(customerMapper).flatMap(cacheCustomer));
    }

    /**
     * Creates a new <code>Customer</code>, and saves the customer to the cache
     *
     * @param model <code>CustomerModel</code> data
     * @return newly created customer
     */
    public Mono<CustomerModel> createCustomer(final CustomerModel model) {
        return customerService.save(Customer.builder().emailAddress(model.getEmailAddress())
                .firstName(model.getFirstName()).lastName(model.getLastName())
                .location(model.getLocation()).build())
                .map(customerMapper).flatMap(cacheCustomer);
    }

    /**
     * Updates a new <code>Customer</code>, and saves the customer to the cache
     *
     * @param model <code>CustomerModel</code> data
     * @return newly created customer
     */
    public Mono<CustomerModel> updateCustomer(final CustomerModel model) {
        return customerService.update(Customer.builder().id(model.getId()).emailAddress(model.getEmailAddress())
                        .firstName(model.getFirstName()).lastName(model.getLastName())
                        .location(model.getLocation()).build())
                .map(customerMapper).flatMap(cacheCustomer);
    }

    public Mono<Void> deleteCustomer(final Long id) {
        return customerService.deleteById(id);
    }
}

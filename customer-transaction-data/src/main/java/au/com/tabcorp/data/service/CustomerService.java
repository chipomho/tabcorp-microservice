package au.com.tabcorp.data.service;

import au.com.tabcorp.common.BeanNames;
import au.com.tabcorp.data.entity.Customer;
import au.com.tabcorp.data.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Customer Service, wrapper for the <code>CustomerRepository</code>
 *
 */
@Service(BeanNames.Data.SERVICE_CUSTOMER)
public class CustomerService {

    /**
     *  Customer Repository Instance
     */
    private final CustomerRepository customerRepository;

    /**
     * Creates a new <code>CustomerService</code> instance and also initialises the required
     * repositories.
     *
     * @param customerRepository <code>CustomerRepository</code> bean instance
     */
    public CustomerService(@Autowired()@Qualifier(BeanNames.Data.REPOSITORY_CUSTOMER)final CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Find the <code>Customer</code> by a given <strong>id</strong>.
     *
     * @param id primary key used to search the customer.
     * @return <code>Customer</code> if one exists.
     */
    public Mono<Customer> customer(final Long id) {
        return customerRepository.findById(id);
    }

    /**
     * Saves a new <code>Customer</code>
     *
     * @param customer <code>Customer</code> instance to save
     * @return the created <code>Customer</code>
     */
    public Mono<Customer> save(final Customer customer) {
        return customerRepository.save(customer);
    }

    /**
     * Updates the existing <code>Customer</code> with a given <strong>id</strong>.
     *
     * @param customer <code>Customer</code> data used to update
     * @return the newly updated <code>Customer</code> instance.
     */
    public Mono<Customer> update(final Customer customer) {
        return customerRepository.findById(customer.getId()).map(Optional::of).defaultIfEmpty(Optional.empty())
                .flatMap(optionalCustomer -> {
                    if (optionalCustomer.isPresent()) {
                        customer.setId(customer.getId());
                        return customerRepository.save(customer);
                    }
                    return Mono.empty();
                });
    }

    /**
     * Deletes the <code>Customer</code> with a given <strong>id</strong>
     *
     * @param id <code>Customer</code> id to delete.
     * @return mono void instance
     */
    public Mono<Void> deleteById(final Long id) {
        return customerRepository.deleteById(id);
    }
}

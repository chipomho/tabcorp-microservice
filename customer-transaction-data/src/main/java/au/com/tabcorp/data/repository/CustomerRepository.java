package au.com.tabcorp.data.repository;

import au.com.tabcorp.common.BeanNames;
import au.com.tabcorp.data.entity.Customer;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository(BeanNames.Data.REPOSITORY_CUSTOMER)
public interface CustomerRepository extends R2dbcRepository<Customer, Long> {
}

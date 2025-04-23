package au.com.tabcorp.data.repository;

import au.com.tabcorp.common.BeanNames;
import au.com.tabcorp.data.entity.Product;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository(BeanNames.Data.REPOSITORY_PRODUCT)
public interface ProductRepository extends R2dbcRepository<Product, String> {
}

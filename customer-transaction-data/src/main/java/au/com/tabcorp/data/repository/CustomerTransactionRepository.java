package au.com.tabcorp.data.repository;

import au.com.tabcorp.common.BeanNames;
import au.com.tabcorp.common.model.CustomerTransactionCostsModel;
import au.com.tabcorp.common.model.LocationTransactionNumberModel;
import au.com.tabcorp.common.model.ProductTransactionCostsModel;
import au.com.tabcorp.data.entity.CustomerTransaction;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Repository(BeanNames.Data.REPOSITORY_CUSTOMER_TRANSACTION)
public interface CustomerTransactionRepository extends R2dbcRepository<CustomerTransaction, Long> {

    @Query("SELECT cus.first_name, cus.last_name , cus.id as id, SUM(prod.product_cost*ct.product_quantity) as costs FROM customer cus JOIN customer_transaction ct ON cus.id = ct.customer_id JOIN product prod ON prod.code = ct.product_code WHERE cus.id=:customerId GROUP BY cus.first_name, cus.last_name, cus.id ")
    Mono<CustomerTransactionCostsModel> costOfTransactionPerCustomer(final Long customerId);

    @Query("SELECT cus.first_name , cus.last_name , cus.id as id, SUM(prod.product_cost*ct.product_quantity) as costs FROM customer cus JOIN customer_transaction ct ON cus.id = ct.customer_id JOIN product prod ON prod.code = ct.product_code GROUP BY cus.first_name, cus.last_name, cus.id")
    Flux<CustomerTransactionCostsModel> costOfTransactionPerCustomer();

    @Query("SELECT prod.code, SUM(prod.product_cost*tran.product_quantity) as costs FROM product prod JOIN customer_transaction tran ON prod.code=tran.product_code WHERE prod.product_status='ACTIVE' GROUP BY prod.code")
    Flux<ProductTransactionCostsModel> costOfTransactionsPerProduct();

    @Query("SELECT prod.code, SUM(prod.product_cost*tran.product_quantity) as costs FROM product prod JOIN customer_transaction tran ON prod.code=tran.product_code WHERE (prod.product_status='ACTIVE') AND (prod.code=:code) GROUP BY prod.code")
    Mono<ProductTransactionCostsModel> costOfTransactionPerProduct(final String code);

    @Query("SELECT c.location, COUNT(t.id) transactions FROM customer_transaction t JOIN customer c ON c.id=t.customer_id WHERE (c.location=:location) GROUP BY c.location")
    Mono<LocationTransactionNumberModel> numberOfTransactionsForLocation(final String location);
}

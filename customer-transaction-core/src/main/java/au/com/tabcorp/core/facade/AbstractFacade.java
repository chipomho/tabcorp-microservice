package au.com.tabcorp.core.facade;

import au.com.tabcorp.common.BeanNames;
import au.com.tabcorp.common.model.CustomerModel;
import au.com.tabcorp.common.model.ProductModel;
import au.com.tabcorp.common.model.TransactionModel;
import au.com.tabcorp.core.service.CacheService;
import au.com.tabcorp.data.entity.Customer;
import au.com.tabcorp.data.entity.CustomerTransaction;
import au.com.tabcorp.data.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.Serializable;
import java.util.function.Function;

import static java.util.Objects.nonNull;

public abstract class AbstractFacade implements Serializable {

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractFacade.class.getName());

    @Autowired()
    @Qualifier(BeanNames.Core.CACHE_SERVICE)
    protected CacheService cacheService;

    protected Function<Customer, CustomerModel> customerMapper = (final Customer customer)
    -> nonNull(customer) ? CustomerModel.builder().id(customer.getId())
            .emailAddress(customer.getEmailAddress()).firstName(customer.getFirstName())
            .lastName(customer.getLastName()).location(customer.getLocation()).build() : null;

    protected Function<CustomerTransaction, TransactionModel> customerTransactionMapper = (final CustomerTransaction transaction)
            -> nonNull(transaction) ? TransactionModel.builder().id(transaction.getId())
            .customerId(transaction.getCustomerId()).transactionTime(transaction.getTransactionTime())
            .quantity(transaction.getQuantity()).productCode(transaction.getProductCode()).build() : null;


    protected Function<Product, ProductModel> productMapper = (final Product product) -> nonNull(product) ? ProductModel.builder().code(product.getCode())
            .cost(product.getCost()).status(product.getStatus()).build() : null;

}

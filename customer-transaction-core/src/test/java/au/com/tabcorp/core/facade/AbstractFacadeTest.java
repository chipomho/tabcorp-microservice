package au.com.tabcorp.core.facade;

import au.com.tabcorp.common.BeanNames;
import au.com.tabcorp.common.enums.ProductStatus;
import au.com.tabcorp.common.model.*;
import au.com.tabcorp.core.CoreBootstrap;
import au.com.tabcorp.core.service.CacheService;
import au.com.tabcorp.data.entity.Customer;
import au.com.tabcorp.data.entity.CustomerTransaction;
import au.com.tabcorp.data.entity.Product;
import au.com.tabcorp.data.service.CustomerService;
import au.com.tabcorp.data.service.CustomerTransactionService;
import au.com.tabcorp.data.service.ProductService;
import au.com.tabcorp.messaging.service.MessageSender;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootTest(classes={ CoreBootstrap.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EnableAutoConfiguration(exclude = R2dbcAutoConfiguration.class)
public abstract class AbstractFacadeTest {

    @MockBean(name = BeanNames.Data.SERVICE_CUSTOMER)
    protected CustomerService customerService;

    @MockBean(name = BeanNames.Data.SERVICE_CUSTOMER_TRANSACTION)
    protected CustomerTransactionService customerTransactionService;

    @MockBean(name = BeanNames.Data.SERVICE_PRODUCT)
    protected ProductService productService;

    @MockBean(name = BeanNames.Core.CACHE_SERVICE)
    protected CacheService cacheService;

    @MockBean(name = BeanNames.Messaging.RABBITMQ_MESSAGE_SENDER)
    protected MessageSender messageSender;

    protected CustomerModel customerModel(){
        return CustomerModel.builder().id(100L).firstName("Tinashe")
                .lastName("Chipomho").emailAddress("tinashe@gmail.com")
                .location("Australia").build();
    }

    protected ProductModel productModel(){
        return ProductModel.builder().cost(new BigDecimal("123.00")).code("PRODUCT_001").status(ProductStatus.ACTIVE).build();
    }

    protected TransactionModel transactionModel(){
        return TransactionModel.builder().id(100L)
                .quantity(123).productCode("PRODUCT_090")
                .customerId(200L).transactionTime(LocalDateTime.now())
                .build();
    }

    protected CustomerTransactionCostsModel customerTransactionCostsModel(){
        return CustomerTransactionCostsModel.builder().costs(new BigDecimal("300.00")).id(100L)
                .firstName("Tinashe").lastName("Chipomho").build();
    }
    protected ProductTransactionCostsModel productTransactionCostsModel(){
        return ProductTransactionCostsModel.builder().costs(new BigDecimal("2345.00"))
                .code("PRODUCT_030").build();
    }

    protected Product product(final ProductModel model){
        return Product.builder().status(model.getStatus())
                .cost(model.getCost()).code(model.getCode()).build();
    }

    protected Customer customer(final CustomerModel model){
        return Customer.builder().id(model.getId()).firstName(model.getFirstName())
                .location(model.getLocation()).lastName(model.getLastName())
                .emailAddress(model.getEmailAddress()).build();
    }

    protected CustomerTransaction customerTransaction(final TransactionModel model){
        return CustomerTransaction.builder().id(model.getId()).customerId(model.getCustomerId())
                .transactionTime(model.getTransactionTime()).productCode(model.getProductCode())
                .quantity(model.getQuantity()).build();
    }

}

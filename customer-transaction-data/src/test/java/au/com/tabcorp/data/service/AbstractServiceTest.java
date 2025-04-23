package au.com.tabcorp.data.service;

import au.com.tabcorp.common.enums.ProductStatus;
import au.com.tabcorp.data.DataBootstrap;
import au.com.tabcorp.data.entity.Customer;
import au.com.tabcorp.data.entity.CustomerTransaction;
import au.com.tabcorp.data.entity.Product;
import au.com.tabcorp.data.repository.CustomerRepository;
import au.com.tabcorp.data.repository.CustomerTransactionRepository;
import au.com.tabcorp.data.repository.ProductRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootTest(classes={ DataBootstrap.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = { "classpath:application.yaml" })
public abstract class AbstractServiceTest {

    @MockBean
    protected CustomerRepository customerRepository;

    @MockBean
    protected CustomerTransactionRepository customerTransactionRepository;

    @MockBean
    protected ProductRepository productRepository;

    protected Customer customer(){
        return Customer.builder().id(10L).firstName("First").lastName("Avenger").emailAddress("fa@gmail.com").build();
    }

    protected CustomerTransaction customerTransaction(){
        return CustomerTransaction.builder().id(100L).transactionTime(LocalDateTime.now().plusDays(1)).customerId(100L).quantity(20).productCode("PRODUCT_001").build();
    }

    protected Product product(){
        return Product.builder().code("PRODUCT_001").cost(new BigDecimal("30.0")).status(ProductStatus.ACTIVE).build();
    }

}

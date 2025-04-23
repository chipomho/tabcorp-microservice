package au.com.tabcorp.toolkit.api;

import au.com.tabcorp.common.BeanNames;
import au.com.tabcorp.common.enums.ProductStatus;
import au.com.tabcorp.common.model.*;
import au.com.tabcorp.core.facade.CustomerFacade;
import au.com.tabcorp.core.facade.ProductFacade;
import au.com.tabcorp.core.facade.ReportingFacade;
import au.com.tabcorp.core.facade.TransactionFacade;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootTest(classes={TabcorpToolkitAPIStarter.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class AbstractTest {

    @MockBean(name= BeanNames.Core.CUSTOMER_FACADE)
    protected CustomerFacade customerFacade;

    @MockBean(name= BeanNames.Core.PRODUCT_FACADE)
    protected ProductFacade productFacade;

    @MockBean(name= BeanNames.Core.REPORTING_FACADE)
    protected ReportingFacade reportingFacade;

    @MockBean(name= BeanNames.Core.TRANSACTION_FACADE)
    protected TransactionFacade transactionFacade;

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
                .customerId(200L).transactionTime(LocalDateTime.now().plusMinutes(30))
                .build();
    }

    protected CustomerTransactionCostsModel customerTransactionCostsModel(){
        return CustomerTransactionCostsModel.builder().costs(new BigDecimal("300.0")).id(100L)
                .firstName("Tinashe").lastName("Chipomho").build();
    }
    protected ProductTransactionCostsModel productTransactionCostsModel(){
        return ProductTransactionCostsModel.builder().costs(new BigDecimal("2345.0"))
                .code("PRODUCT_030").build();
    }

}

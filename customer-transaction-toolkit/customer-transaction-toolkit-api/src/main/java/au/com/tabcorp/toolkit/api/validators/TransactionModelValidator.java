package au.com.tabcorp.toolkit.api.validators;

import au.com.tabcorp.common.BeanNames;
import au.com.tabcorp.common.enums.ConstraintType;
import au.com.tabcorp.common.enums.ProductStatus;
import au.com.tabcorp.common.model.CustomerModel;
import au.com.tabcorp.common.model.ProductModel;
import au.com.tabcorp.common.model.TransactionModel;
import au.com.tabcorp.core.facade.CustomerFacade;
import au.com.tabcorp.core.facade.ProductFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Component(BeanNames.ToolkitAPI.VALIDATOR_TRANSACTION_MODEL)
public class TransactionModelValidator extends AbstractValidator<TransactionModel> {

    /**
     * Product Facade
     */
    @Autowired()
    @Qualifier(BeanNames.Core.PRODUCT_FACADE)
    protected ProductFacade productFacade;

    @Autowired()
    @Qualifier(BeanNames.Core.CUSTOMER_FACADE)
    protected CustomerFacade customerFacade;

    @Value("${tabcorp.app.validation.transactions.maximum-cost:5000.00}")
    protected Double maximumCost;


    public Mono<BindingResult> validate(final TransactionModel transactionModel) {
        return Mono.just(transactionModel).flatMap(model -> {
            final BindingResult result = new BeanPropertyBindingResult(model,"transactionModel");
            if (isNull(model.getTransactionTime())){
                result.rejectValue("transactionTime", code("transactionTime", ConstraintType.NULL), "Transaction time is required");
            }
            else{
                if (LocalDateTime.now().isAfter(model.getTransactionTime())){
                    result.rejectValue("transactionTime", code("transactionTime",ConstraintType.VALID),"Transaction Date & Time must not be in the past");
                }
            }

            if (isNull(model.getQuantity()) || (model.getQuantity() <= 0) ){
                result.rejectValue("quantity", code("quantity", ConstraintType.NULL), "Valid Quantity is required");
            }

            if (isBlank(model.getProductCode())){
                result.rejectValue("productCode", code("productCode", ConstraintType.BLANK), "Product code is required");
            }

            if (isNull(model.getCustomerId()) || (model.getCustomerId() <= 0)){
                result.rejectValue("customerId", code("customerId", ConstraintType.NULL), "A valid Customer ID is required");
            }

            return Mono.just(result)
                   .flatMap(errors ->{
                        if (!errors.hasErrors()){
                            return Mono.just(errors).zipWith(customerFacade.customer(model.getCustomerId())
                            .defaultIfEmpty(CustomerModel.builder().build()))
                            .map(tuple -> {
                                final CustomerModel customer = tuple.getT2();
                                if (isNull(customer.getId())){
                                    errors.rejectValue("customerId", code("customerId", ConstraintType.MATCH), "Customer ID is required");
                                }
                                return tuple.getT1();
                            });
                        }
                        return Mono.just(errors);
                   })
                   .flatMap(errors->{
                      if (!errors.hasErrors()){
                        return Mono.just(errors).zipWith( productFacade.product(model.getProductCode()).defaultIfEmpty(ProductModel.builder().build()) )
                                .map(tuple -> {
                                    final ProductModel product = tuple.getT2();
                                    if (isBlank(product.getCode())){
                                        errors.rejectValue("productCode", code("productCode", ConstraintType.MATCH), "A valid Product code is required");
                                    }
                                    else{
                                        if (!ProductStatus.ACTIVE.equals(product.getStatus())){
                                            errors.rejectValue("productCode", code("productCode", ConstraintType.STATUS), "Product must be active");
                                        }

                                        if (model.getQuantity().doubleValue() * product.getCost().doubleValue() > maximumCost) {
                                            errors.rejectValue("productCode", code("productCode", ConstraintType.MAXIMUM), String.format("Total cost of transaction must not exceed %02f", maximumCost) );
                                        }
                                    }
                                    return tuple.getT1();
                                });
                      }
                      return Mono.just(errors);
                   });
        });

    }

}

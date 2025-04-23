package au.com.tabcorp.toolkit.api.validators;

import au.com.tabcorp.common.BeanNames;
import au.com.tabcorp.common.enums.ConstraintType;
import au.com.tabcorp.common.model.ProductModel;
import au.com.tabcorp.core.facade.ProductFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import reactor.core.publisher.Mono;

import static au.com.tabcorp.common.model.ProductModel.MAXIMUM_LENGTH_PRODUCT_CODE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Component(BeanNames.ToolkitAPI.VALIDATOR_PRODUCT_MODEL)
public class ProductModelValidator extends AbstractValidator<ProductModel> {

    /**
     * Inject <code>ProductFacade</code> bean instance
     */
    @Autowired()
    @Qualifier(BeanNames.Core.PRODUCT_FACADE)
    protected ProductFacade productFacade;

    protected void checkProductData(final BindingResult result, final ProductModel model) {
        if (nonNull(model)) {
            if (isBlank(model.getCode())) {
                result.rejectValue("code", code("code", ConstraintType.BLANK), "Product 'Code' is required");
            } else {
                rejectIfLengthIsGreaterThanMaximum(result, model.getCode(),
                        MAXIMUM_LENGTH_PRODUCT_CODE, "code",
                        code("code", ConstraintType.LENGTH), String.format("Product 'Code' is too long, maximum length is: %d", MAXIMUM_LENGTH_PRODUCT_CODE));
            }

            if (isNull(model.getStatus())) {
                result.rejectValue("status", code("status", ConstraintType.NULL), "Product 'Status' is required");
            }

            if (isNull(model.getCost())){
                result.rejectValue("cost", code("cost", ConstraintType.NULL), "Product 'Cost' is required");
            }
            else{
                if (model.getCost().doubleValue()<=0){
                    result.rejectValue("cost", code("cost", ConstraintType.VALID), "Product 'Cost' minimum value must be greater than '0'");
                }
            }
        }
    }

    public Mono<BindingResult> validateCreate(final ProductModel productModel) {
        return Mono.just(productModel).flatMap(model -> {
            final BindingResult result = new BeanPropertyBindingResult(model, "customerModel");
            checkProductData(result, model);
            return Mono.just(result).flatMap(errors->{
                if (!errors.hasErrors()){
                    return Mono.just(errors).zipWith(productFacade.product(model.getCode())
                                    .defaultIfEmpty(ProductModel.builder().build()))
                            .map(tuple -> {
                                final ProductModel product = tuple.getT2();
                                if (nonNull(product.getCode())){
                                    errors.rejectValue("code", code("code", ConstraintType.EXISTS), String.format("Product Code %s already exists.", model.getCode()));
                                }
                                return tuple.getT1();
                            });
                }
                return Mono.just(errors);
            });
        });
    }

    public Mono<BindingResult> validateUpdate(final ProductModel customerModel) {
        return Mono.just(customerModel).flatMap(model -> {
            final BindingResult result = new BeanPropertyBindingResult(model, "customerModel");
            checkProductData(result, model);
            return Mono.just(result).flatMap(errors->{
                if (!errors.hasErrors()){
                    return Mono.just(errors).zipWith(productFacade.product(model.getCode())
                                    .defaultIfEmpty(ProductModel.builder().build()))
                            .map(tuple -> {
                                final ProductModel product = tuple.getT2();
                                if (isNull(product.getCode())){
                                    errors.rejectValue("code", code("code", ConstraintType.VALID), "Product to update is not valid or doesn't exist.");
                                }
                                return tuple.getT1();
                            });
                }
                return Mono.just(errors);
            });
        });
    }

}

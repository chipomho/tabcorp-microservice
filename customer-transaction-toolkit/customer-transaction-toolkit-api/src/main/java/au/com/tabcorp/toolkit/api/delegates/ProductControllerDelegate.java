package au.com.tabcorp.toolkit.api.delegates;

import au.com.tabcorp.common.BeanNames;
import au.com.tabcorp.common.model.CustomerModel;
import au.com.tabcorp.common.model.ProductModel;
import au.com.tabcorp.core.annotations.Delegate;
import au.com.tabcorp.core.facade.CustomerFacade;
import au.com.tabcorp.core.facade.ProductFacade;
import au.com.tabcorp.toolkit.api.validators.CustomerModelValidator;
import au.com.tabcorp.toolkit.api.validators.ProductModelValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import reactor.core.publisher.Mono;

@Delegate(BeanNames.ToolkitAPI.DELEGATE_PRODUCT)
public class ProductControllerDelegate extends AbstractDelegate{

    protected final ProductFacade productFacade;
    protected final ProductModelValidator productModelValidator;

    public ProductControllerDelegate(@Autowired()@Qualifier(BeanNames.Core.PRODUCT_FACADE)final ProductFacade productFacade,
                                     @Autowired()@Qualifier(BeanNames.ToolkitAPI.VALIDATOR_PRODUCT_MODEL)final ProductModelValidator productModelValidator) {
        this.productFacade = productFacade;
        this.productModelValidator = productModelValidator;
    }

    public Mono<ProductModel> getProductByCode(final String code){
      return productFacade.product(code);
    }

    public Mono<BindingResult> validateCreate(final ProductModel model) {
        return productModelValidator.validateCreate(model);
    }

    public Mono<BindingResult> validateUpdate(final ProductModel model) {
        return productModelValidator.validateUpdate(model);
    }

    public Mono<ProductModel> createProduct(final ProductModel data) {
        return productFacade.createProduct( data );
    }

    public Mono<ProductModel> updateProduct(final ProductModel data) {
        return productFacade.updateProduct( data );
    }

    public Mono<Void> deleteProduct(final String code){
        return productFacade.deleteProduct(code);
    }
}

package au.com.tabcorp.core.facade;

import au.com.tabcorp.common.BeanNames;
import au.com.tabcorp.common.model.CustomerModel;
import au.com.tabcorp.common.model.ProductModel;
import au.com.tabcorp.core.annotations.Facade;
import au.com.tabcorp.data.entity.Product;
import au.com.tabcorp.data.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.trim;

@Facade(BeanNames.Core.PRODUCT_FACADE)
public class ProductFacade extends AbstractFacade {

    protected final ProductService productService;

    /**
     * Function to cache the ProductModel
     */
    protected Function<ProductModel, Mono<ProductModel>> cacheProduct = (final ProductModel model)
            -> {
        try{
            return cacheService.set(productKey(model.getCode()),model).thenReturn(model);
        }
        catch (Exception e) {
            return Mono.error(e);
        }
    };

    protected static String productKey(final String code) {
        return String.format("Product:%s",trim(code));
    }

    public ProductFacade(@Autowired()@Qualifier(BeanNames.Data.SERVICE_PRODUCT) final ProductService productService) {
        this.productService = productService;
    }

    /**
     * Cache first, then the database, if database save to cache
     *
     * @param productCode product code
     * @return
     */
    public Mono<ProductModel> product(final String productCode){
       return cacheService.get(productKey(productCode),ProductModel.class)
               .switchIfEmpty( productService.getProductByCode(productCode).switchIfEmpty(Mono.empty())
                       .map(productMapper).flatMap(cacheProduct));
    };

    /**
     * Create and cache
     *
     * @param model product model
     * @return
     */
    public Mono<ProductModel> createProduct(final ProductModel model) {
        return productService.save(Product.builder().code(model.getCode())
                        .cost(model.getCost()).status(model.getStatus()).build())
                .map(productMapper).flatMap(cacheProduct);
    }

    public Mono<ProductModel> updateProduct(final ProductModel model) {
        return productService.update(Product.builder().code(model.getCode()).status(model.getStatus())
                        .cost(model.getCost()).build())
                .map(productMapper).flatMap(cacheProduct);
    }

    public Mono<Void> deleteProduct(final String code) {
        return productService.deleteById(code);
    }

}

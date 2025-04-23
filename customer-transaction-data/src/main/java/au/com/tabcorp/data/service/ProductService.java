package au.com.tabcorp.data.service;

import au.com.tabcorp.common.BeanNames;
import au.com.tabcorp.data.entity.Product;
import au.com.tabcorp.data.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service(BeanNames.Data.SERVICE_PRODUCT)
public class ProductService {
    protected final ProductRepository productRepository;

    public ProductService(@Autowired()@Qualifier(BeanNames.Data.REPOSITORY_PRODUCT) final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public Mono<Product> getProductByCode(final String code){
        return productRepository.findById(code);
    }

    public Mono<Product> save(final Product product) {
        return productRepository.save(product);
    }

    public Mono<Product> update(final Product product) {
        return productRepository.findById(product.getCode()).map(Optional::of).defaultIfEmpty(Optional.empty())
                .flatMap(optionalProduct -> {
                    if (optionalProduct.isPresent()) {
                        product.setCode(product.getCode());
                        return productRepository.save(product);
                    }
                    return Mono.empty();
                });
    }

    public Mono<Void> deleteById(final String code) {
        return productRepository.deleteById(code);
    }
}

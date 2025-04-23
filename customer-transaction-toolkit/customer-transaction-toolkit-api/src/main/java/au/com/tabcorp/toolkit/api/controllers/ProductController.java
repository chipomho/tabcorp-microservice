package au.com.tabcorp.toolkit.api.controllers;

import au.com.tabcorp.common.model.ProductModel;
import au.com.tabcorp.toolkit.api.delegates.ProductControllerDelegate;
import au.com.tabcorp.toolkit.api.utils.WebPathMappings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping(WebPathMappings.CONTROLLER_PRODUCT)
@PreAuthorize("hasAuthority('PRODUCT')")
public class ProductController extends AbstractController<ProductControllerDelegate> {

    public ProductController(final ProductControllerDelegate delegate) {
        super(delegate);
    }

    @GetMapping("/{code}")
    public Mono<ResponseEntity<ProductModel>> getProductByCode(@PathVariable("code") final String code) {
        return d().getProductByCode(code)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @PostMapping()
    public Mono<ResponseEntity<?>> createProduct(@RequestBody final Mono<ProductModel> productData) {
        return productData.flatMap(product->
                d().validateCreate(product)
                        .flatMap(errors->{
                            if (errors.hasErrors()) {
                                return Mono.just(new ResponseEntity<>( wrap(d().build(errors)), HttpStatus.BAD_REQUEST) );
                            }
                            return d().createProduct(product)
                                    .map(result->new ResponseEntity<>(result,HttpStatus.CREATED)) ;
                        })
        );
    }

    @PutMapping()
    public Mono<ResponseEntity<?>> updateProduct(@RequestBody final Mono<ProductModel> productData) {
        return productData.flatMap(product->
                d().validateUpdate(product)
                        .flatMap(errors->{
                            if (errors.hasErrors()) {
                                return Mono.just(new ResponseEntity<>( wrap(d().build(errors)), HttpStatus.BAD_REQUEST) );
                            }
                            return d().updateProduct(product)
                                    .map(result->new ResponseEntity<>(result,HttpStatus.OK)) ;
                        })
        );
    }

    @DeleteMapping("/{code}")
    public Mono<Void> deleteProduct(@PathVariable("code") final String code) {
        return d().deleteProduct(code);
    }




}

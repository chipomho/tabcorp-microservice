package au.com.tabcorp.toolkit.api.controllers;

import au.com.tabcorp.common.BeanNames;
import au.com.tabcorp.common.model.AuthenticationRequestModel;
import au.com.tabcorp.toolkit.api.delegates.AuthenticationControllerDelegate;
import au.com.tabcorp.toolkit.api.utils.WebPathMappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping(WebPathMappings.CONTROLLER_AUTHENTICATION)
public class AuthenticationController extends AbstractController<AuthenticationControllerDelegate>{

    protected AuthenticationController(@Autowired()@Qualifier(BeanNames.ToolkitAPI.DELEGATE_AUTHENTICATION) final AuthenticationControllerDelegate delegate) {
        super(delegate);
    }


    @PostMapping(WebPathMappings.AUTHENTICATION_TOKEN)
    public Mono<ResponseEntity<?>> authenticate(@RequestBody()final Mono<AuthenticationRequestModel> authenticationData){
        return authenticationData.flatMap(authentication-> d().validate(authentication)
                .flatMap(errors->{
                    if (errors.hasErrors()) {
                        return Mono.just(new ResponseEntity<>( wrap(d().build(errors))  , HttpStatus.UNAUTHORIZED) );
                    }
                    return d().authenticate(authentication)
                            .map(result->new ResponseEntity<>(result,HttpStatus.OK)) ;
                }) );
    }

}

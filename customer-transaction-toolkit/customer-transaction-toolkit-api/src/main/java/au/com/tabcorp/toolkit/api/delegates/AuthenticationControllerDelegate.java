package au.com.tabcorp.toolkit.api.delegates;

import au.com.tabcorp.common.BeanNames;
import au.com.tabcorp.common.model.AuthenticationRequestModel;
import au.com.tabcorp.common.model.AuthenticationResponseModel;
import au.com.tabcorp.core.annotations.Delegate;
import au.com.tabcorp.toolkit.api.components.AuthenticationManager;
import au.com.tabcorp.toolkit.api.validators.AuthenticationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.BindingResult;
import reactor.core.publisher.Mono;

@Delegate(BeanNames.ToolkitAPI.DELEGATE_AUTHENTICATION)
public class AuthenticationControllerDelegate extends AbstractDelegate {

    protected final AuthenticationValidator authenticationValidator;
    protected final AuthenticationManager authenticationManager;

    public AuthenticationControllerDelegate(@Autowired()@Qualifier(BeanNames.ToolkitAPI.VALIDATOR_AUTHENTICATION) final AuthenticationValidator authenticationValidator,
                                            final AuthenticationManager authenticationManager) {
        this.authenticationValidator = authenticationValidator;
        this.authenticationManager = authenticationManager;
    }

    public Mono<BindingResult> validate(final AuthenticationRequestModel authentication){
      return authenticationValidator.validate(authentication );
    }

    public Mono<AuthenticationResponseModel> authenticate(final AuthenticationRequestModel authentication){
      return Mono.just(authenticationManager.authenticate(authentication, "api.tabcorp.com.au" ) ); //should read hostname from request or set one in properties
    }
}

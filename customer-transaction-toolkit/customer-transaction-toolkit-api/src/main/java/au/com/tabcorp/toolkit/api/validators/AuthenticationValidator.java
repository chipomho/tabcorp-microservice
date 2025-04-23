package au.com.tabcorp.toolkit.api.validators;

import au.com.tabcorp.common.BeanNames;
import au.com.tabcorp.common.enums.ConstraintType;
import au.com.tabcorp.common.model.AuthenticationRequestModel;
import au.com.tabcorp.toolkit.api.properties.InMemoryUserManagerProperties;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import reactor.core.publisher.Mono;

import static org.apache.commons.lang3.StringUtils.trim;

@Component(BeanNames.ToolkitAPI.VALIDATOR_AUTHENTICATION)
public class AuthenticationValidator extends AbstractValidator<AuthenticationRequestModel> {

    @Autowired()
    protected InMemoryUserManagerProperties userManagerProperties;

    public Mono<BindingResult> validate(final AuthenticationRequestModel model) {
        return Mono.just(model).flatMap(authentication->{
            final BindingResult result = new BeanPropertyBindingResult(authentication, "authenticationModel");
            if (CollectionUtils.isEmpty(userManagerProperties.getUsers())){
                result.rejectValue("clientId", code("clientId", ConstraintType.VALID), "Authorisation is possible" );
            }
            else{
                //if the user details are not found add error
              if (userManagerProperties.getUsers().stream().noneMatch(user -> StringUtils.equals(trim(user.getUsername()), trim(authentication.getClientId()))
                      && StringUtils.equals(trim(user.getPassword()), trim(authentication.getClientSecret())))){
                  result.rejectValue("clientId", code("clientId", ConstraintType.MATCH), "Supplied credentials are incorrect" );
              }
            }
            return Mono.just(result);
        });
    }

}

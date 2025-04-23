package au.com.tabcorp.toolkit.api.validators;

import au.com.tabcorp.common.BeanNames;
import au.com.tabcorp.common.enums.ConstraintType;
import au.com.tabcorp.common.model.CustomerModel;
import au.com.tabcorp.core.facade.CustomerFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import reactor.core.publisher.Mono;

import static au.com.tabcorp.common.model.CustomerModel.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Component(BeanNames.ToolkitAPI.VALIDATOR_CUSTOMER_MODEL)
public class CustomerModelValidator extends AbstractValidator<CustomerModel> {

    /**
     * Inject <code>CustomerFacade</code> bean instance
     */
    @Autowired()
    @Qualifier(BeanNames.Core.CUSTOMER_FACADE)
    protected CustomerFacade customerFacade;

    protected void checkCustomerData(final BindingResult result, final CustomerModel model) {
        if (nonNull(model)) {
            if (isBlank(model.getFirstName())) {
                result.rejectValue("firstName", code("firstName", ConstraintType.BLANK), "Customer 'First Name' is required");
            } else {
                rejectIfLengthIsGreaterThanMaximum(result, model.getFirstName(),
                        MAXIMUM_LENGTH_CUSTOMER_FIRST_NAME, "firstName",
                        code("firstName", ConstraintType.LENGTH), String.format("Customer 'First Name' is too long, maximum length is: %d", MAXIMUM_LENGTH_CUSTOMER_FIRST_NAME));
            }

            if (isBlank(model.getLastName())) {
                result.rejectValue("lastName", code("lastName", ConstraintType.BLANK), "Customer 'Last Name' is required");
            } else {
                rejectIfLengthIsGreaterThanMaximum(result, model.getLastName(),
                        MAXIMUM_LENGTH_CUSTOMER_LAST_NAME, "lastName",
                        code("lastName", ConstraintType.LENGTH), String.format("Customer 'Last Name' is too long, maximum length is: %d", MAXIMUM_LENGTH_CUSTOMER_LAST_NAME));
            }

            if (isBlank(model.getEmailAddress())) {
                result.rejectValue("emailAddress", code("emailAddress", ConstraintType.BLANK), "Customer 'Email Address' is required");
            } else {
                rejectIfLengthIsGreaterThanMaximum(result, model.getEmailAddress(),
                        MAXIMUM_LENGTH_CUSTOMER_EMAIL_ADDRESS, "emailAddress",
                        code("emailAddress", ConstraintType.LENGTH), String.format("Customer 'Email Address' is too long, maximum length is: %d", MAXIMUM_LENGTH_CUSTOMER_EMAIL_ADDRESS));
            }

            if (isBlank(model.getLocation())) {
                result.rejectValue("location", code("location", ConstraintType.BLANK), "Customer 'Location' is required");
            } else {
                rejectIfLengthIsGreaterThanMaximum(result, model.getLocation(),
                        MAXIMUM_LENGTH_CUSTOMER_LOCATION, "location",
                        code("location", ConstraintType.LENGTH), String.format("Customer 'Location' is too long, maximum length is: %d", MAXIMUM_LENGTH_CUSTOMER_LOCATION));
            }
        }
    }

    public Mono<BindingResult> validateCreate(final CustomerModel customerModel) {
        return Mono.just(customerModel).flatMap(model -> {
            final BindingResult result = new BeanPropertyBindingResult(model, "customerModel");
            checkCustomerData(result, model);
            return Mono.just(result);
        });
    }

    public Mono<BindingResult> validateUpdate(final CustomerModel customerModel) {
        return Mono.just(customerModel).flatMap(model -> {
            final BindingResult result = new BeanPropertyBindingResult(model, "customerModel");
            checkCustomerData(result, model);
            return Mono.just(result).flatMap(errors->{
                if (!errors.hasErrors()){
                    return Mono.just(errors).zipWith(customerFacade.customer(model.getId())
                                    .defaultIfEmpty(CustomerModel.builder().build()))
                            .map(tuple -> {
                                final CustomerModel customer = tuple.getT2();
                                if (isNull(customer.getId())){
                                    errors.rejectValue("id", code("id", ConstraintType.VALID), "Customer to update is not valid or doesn't exist.");
                                }
                                return tuple.getT1();
                            });
                }
                return Mono.just(errors);
            });
        });
    }

}

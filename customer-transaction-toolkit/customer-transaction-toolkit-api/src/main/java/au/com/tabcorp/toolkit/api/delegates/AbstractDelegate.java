package au.com.tabcorp.toolkit.api.delegates;

import au.com.tabcorp.common.model.ValidationErrorModel;
import au.com.tabcorp.toolkit.api.validators.AbstractValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class AbstractDelegate implements Serializable {

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractDelegate.class.getName());

    @Autowired()
    protected MessageSource messageSource;

    public List<ValidationErrorModel> build(final BindingResult errors) {
        return errors.hasErrors() ? errors.getFieldErrors().stream()
                .map(e->ValidationErrorModel.builder().field(e.getField())
                        .code(e.getCode()).message(messageSource.getMessage(Objects.requireNonNull(e.getCode()), e.getArguments(), e.getDefaultMessage(), Locale.getDefault())).build()).collect(Collectors.toList()): new ArrayList<>();
    }

}

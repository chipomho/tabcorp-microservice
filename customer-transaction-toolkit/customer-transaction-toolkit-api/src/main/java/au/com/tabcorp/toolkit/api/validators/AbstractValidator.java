package au.com.tabcorp.toolkit.api.validators;

import au.com.tabcorp.common.enums.ConstraintType;
import au.com.tabcorp.common.model.ValidationErrorModel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public abstract class AbstractValidator<M> implements Serializable {

    /**
     * Validator Logger
     */
    protected static final Logger LOG = LoggerFactory.getLogger(AbstractValidator.class.getName());


    protected static final String MESSAGE_SUFFIX = "Model";

    @Autowired()
    protected MessageSource messageSource;

    protected final String messagePrefix;

    @Value("${app.validation.messages.validator-prefix:ValidationMessages}")
    protected String messagePropertyPrefix;

    protected AbstractValidator() {
        final Class<M> validatorClazz = (Class<M>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        String className = validatorClazz.getSimpleName();
        if (className.endsWith(MESSAGE_SUFFIX)){
            className = className.substring(0, (className.length()-MESSAGE_SUFFIX.length()));
        }
        this.messagePrefix = className;
    }

    protected AbstractValidator(final String messagePrefix) {
        this.messagePrefix = messagePrefix;
    }

    public List<ValidationErrorModel> build(final BindingResult errors) {
        return errors.hasErrors() ? errors.getFieldErrors().stream()
                .map(e->ValidationErrorModel.builder().field(e.getField())
                        .code(e.getCode()).message(messageSource.getMessage(Objects.requireNonNull(e.getCode()), e.getArguments(), e.getDefaultMessage(), Locale.getDefault())).build()).collect(Collectors.toList()): new ArrayList<>();
    }

    /**
     * Returns message code prefix.
     *
     * @return
     */
    protected String getMessagePrefix(){
        return isNotBlank(messagePropertyPrefix) ? String.format("%s.%s",trim(messagePropertyPrefix), messagePrefix) : "";
    }

    /**
     * Field code messages using the message code prefix if available.
     *
     * @param fieldName
     * @param constraints
     * @return
     */
    public String code(String fieldName, ConstraintType constraints){
        return String.format("%s%s.%s", isNotBlank(getMessagePrefix()) ? String.format("%s.",getMessagePrefix()) :"",fieldName,constraints.getCode());
    }

    public String code(final String prefix, final String fieldName, final ConstraintType constraint){
        return String.format("%s.%s.%s", prefix , fieldName, constraint.getCode());
    }

    protected void l(final Errors errors, final String name, final String value, final int max, final String message){
        l(errors, name, value, max, message, code(name,ConstraintType.LENGTH) );
    }

    protected void l(final Errors errors, final String name, final String value, final int max, final String message, final String messageCode){
        if (StringUtils.isNotBlank(value)){
            if (value.length() > max){
                errors.rejectValue(name, messageCode, new Object[]{max, value.length()}, message);
            }
        }
    }

    protected void rejectIfLengthIsGreaterThanMaximum(final BindingResult result, final String value, final int length, final String fieldName, final String errorCode, final String defaultMessage){
        if (nonNull(value) && (value.length() > length)  ){
            result.rejectValue(fieldName, errorCode, new Object[]{length}, defaultMessage);
        }
    }

    protected void rejectIfLengthIsLessThanMinimum(final BindingResult result, final String value, final int length, final String fieldName, final String errorCode, final String defaultMessage){
        if (nonNull(value) && (value.length() < length)  ){
            result.rejectValue(fieldName, errorCode, new Object[]{length}, defaultMessage);
        }
    }
}

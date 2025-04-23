package au.com.tabcorp.toolkit.api.controllers;

import au.com.tabcorp.common.model.ValidationErrorModel;
import au.com.tabcorp.toolkit.api.delegates.AbstractDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <code>AbstractController</code> and uses only one delegate
 *
 * @param <D>
 */
public abstract class AbstractController<D extends AbstractDelegate> {

    /**
     * Controller Logger
     */
    protected static final Logger LOG = LoggerFactory.getLogger(AbstractController.class.getName());

    /**
     * Controller Delegate
     */
    private final D delegate;

    protected AbstractController(final D delegate) {
        this.delegate = delegate;
    }

    /**
     * Controller Delegate
     * @return return the current controller delegate
     */
    protected D d() {
        return delegate;
    }

    /**
     * Wraps the Validation List into a Map to properly format the response object.
     *
     * @param errors list of errors
     * @return <code>Map</code> with status code and list of errors.
     */
    protected Map<String,Object> wrap(final List<ValidationErrorModel> errors){
        return Stream.of(new AbstractMap.SimpleEntry<>("status", HttpStatus.BAD_REQUEST.getReasonPhrase()), new AbstractMap.SimpleEntry<>("errors", errors ))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}

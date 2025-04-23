package au.com.tabcorp.toolkit.api.controllers;

import au.com.tabcorp.common.model.UserModel;
import au.com.tabcorp.toolkit.api.AbstractTest;
import au.com.tabcorp.toolkit.api.components.AuthenticationManager;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Arrays;

@EnableAutoConfiguration(exclude = R2dbcAutoConfiguration.class)
@AutoConfigureWebTestClient()
public abstract class AbstractControllerTest extends AbstractTest {

    @Autowired()
    protected WebTestClient webTestClient;

    @Autowired()
    protected AuthenticationManager authenticationManager;

    protected String token;

    protected String createToken(String username, String ... roles) {
        return String.format("Bearer " + authenticationManager.createToken(UserModel.builder().username(username)
                .roles(Arrays.asList(roles)).build(),"localhost"));
    }

    @BeforeEach()
    void beforeEach() {
        token = createToken("admin","STANDARD","PRODUCT","CUSTOMER");
    }

}

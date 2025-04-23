package au.com.tabcorp.toolkit.api.configuration;

import au.com.tabcorp.toolkit.api.components.JwtSecurityContextRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;

@Configuration
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity()
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(final ServerHttpSecurity http,
                                                         @Autowired() JwtSecurityContextRepository contextRepository) {
        http.csrf().disable()
            .httpBasic().disable()
            .formLogin().disable()
            .securityContextRepository(contextRepository)
            .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/authentication**/*").permitAll()
                        .anyExchange().authenticated() );
        return http.build();

    }


}

package au.com.tabcorp.toolkit.api.components;

import au.com.tabcorp.common.BeanNames;
import au.com.tabcorp.common.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

@Component()
public class JwtSecurityContextRepository implements ServerSecurityContextRepository {

    @Autowired()
    @Qualifier(BeanNames.ToolkitAPI.COMPONENT_AUTHENTICATION_MANAGER)
    protected AuthenticationManager authenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    /**
     * Checks for the <strong>JWT</strong> token in the <code>HttpHeaders.AUTHORIZATION</code> header
     * @param exchange ServerWebExchange
     * @return
     */
    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
       final String authHeader = trimToEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
       if (isNotBlank(authHeader) && authHeader.startsWith("Bearer ")){
           String authToken = authHeader.substring(7);
           final UserModel user = authenticationManager.isValidToken(authToken) ?  authenticationManager.fromToken(authToken) : null;
           if (nonNull(user)){

               final List<GrantedAuthority> authorities = user.getRoles().stream()
                       .map(SimpleGrantedAuthority::new).collect(Collectors.toList());

               return Mono.just(new UsernamePasswordAuthenticationToken(
                       user.getUsername(), null, authorities
               )).map(SecurityContextImpl::new);
           }
       }
       return Mono.empty();
    }
}


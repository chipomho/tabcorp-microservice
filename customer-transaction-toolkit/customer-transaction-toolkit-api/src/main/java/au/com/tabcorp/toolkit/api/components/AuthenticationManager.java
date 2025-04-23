package au.com.tabcorp.toolkit.api.components;

import au.com.tabcorp.common.BeanNames;
import au.com.tabcorp.common.model.AuthenticationRequestModel;
import au.com.tabcorp.common.model.AuthenticationResponseModel;
import au.com.tabcorp.common.model.UserModel;
import au.com.tabcorp.toolkit.api.properties.InMemoryUserManagerProperties;
import au.com.tabcorp.toolkit.api.utils.RSAKeyUtils;
import au.com.tabcorp.toolkit.api.utils.WebPathMappings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.*;

import static java.util.Objects.nonNull;

@Component(BeanNames.ToolkitAPI.COMPONENT_AUTHENTICATION_MANAGER)
public class AuthenticationManager {

    /**
     * Authentication Manager using simple in-memory user cache
     */
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationManager.class.getName());

    @Autowired()
    protected InMemoryUserManagerProperties userManagerProperties;

    @Autowired()
    protected Environment environment;

    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    @PostConstruct()
    void init() throws Exception {
        publicKey = RSAKeyUtils.readX509PublicKey( environment.getRequiredProperty("tabcorp.security.rsa.public-key", String.class) );
        privateKey = RSAKeyUtils.readPKCS8PrivateKey( environment.getRequiredProperty("tabcorp.security.rsa.private-key", String.class)  );
        if (LOG.isInfoEnabled()){
            LOG.info("Authentication Manager initialised....");
        }

    }

    public AuthenticationResponseModel authenticate(final AuthenticationRequestModel authentication, final String hostname) {
        final UserModel user = userManagerProperties.getUserByUsername(authentication.getClientId());
        final String token = createToken(user, hostname);
        return AuthenticationResponseModel.builder()
                .expiresIn(userManagerProperties.getExpiresIn()).roles(user.getRoles())
                .token(token).refreshToken(createRefreshToken(user.getUsername())).build();
    }

    public Claims parseJWT(final String token) {
        try{
          return  Jwts.parser().verifyWith(publicKey)
                    .build().parseSignedClaims(token).getPayload();
        }
        catch (Exception ex){}
        return null;
    }

    public boolean isValidToken(final String token) {
        return nonNull(parseJWT(token));
    }

    public UserModel fromToken(final String token) {
        final Claims claims =  parseJWT(token);
        return nonNull(claims) ? userManagerProperties.getUserByUsername(claims.getSubject()) : null;
    }

    public String createToken(final UserModel user, final String hostname) {

        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.SECOND, userManagerProperties.getExpiresIn());
        final Date expiration = calendar.getTime();
        final Map<String,String> headers = new HashMap<>();
        headers.put("X-Micro-Service-Name","Technical Test");
        headers.put("X-Micro-Service-Author","Tinashe Chipomho");
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("user", UserModel.builder().username(user.getUsername()).roles(user.getRoles()).build())
                .issuedAt(new Date())
                .expiration(expiration)
                .header().add(headers).and()
                .issuer( String.format("https://%s%s%s",hostname, WebPathMappings.CONTROLLER_AUTHENTICATION,WebPathMappings.AUTHENTICATION_TOKEN)  )
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();
    }

    public String createRefreshToken(final String username) {
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.SECOND, userManagerProperties.getExpiresIn());
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(calendar.getTime())
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();
    }
}

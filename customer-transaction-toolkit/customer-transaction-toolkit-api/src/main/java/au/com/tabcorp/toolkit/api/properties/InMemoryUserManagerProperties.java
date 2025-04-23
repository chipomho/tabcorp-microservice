package au.com.tabcorp.toolkit.api.properties;

import au.com.tabcorp.common.model.UserModel;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Setter()
@Getter()
@Builder()
@AllArgsConstructor()
@NoArgsConstructor()
@ConfigurationProperties(prefix = "tabcorp.security.in-memory-users")
public class InMemoryUserManagerProperties {

    @Builder.Default
    private Integer expiresIn = 900;

    private List<UserModel> users;


    public UserModel getUserByUsername(String username) {
        return users.stream().filter(user -> user.getUsername().equals(username)).findFirst().orElse(null);
    }

}

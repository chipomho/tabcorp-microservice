package au.com.tabcorp.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data()
@AllArgsConstructor()
@NoArgsConstructor()
@Builder()
@EqualsAndHashCode()
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResponseModel implements Serializable {

    @JsonProperty("token")
    private String token;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("roles")
    private List<String> roles;

    @JsonProperty("expires_in")
    private Integer expiresIn;
}

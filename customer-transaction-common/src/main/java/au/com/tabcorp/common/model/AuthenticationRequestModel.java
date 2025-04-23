package au.com.tabcorp.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Data()
@AllArgsConstructor()
@NoArgsConstructor()
@Builder()
@EqualsAndHashCode()
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationRequestModel implements Serializable {

    @JsonProperty("client-id")
    private String clientId;

    @JsonProperty("client-secret")
    private String clientSecret;

}

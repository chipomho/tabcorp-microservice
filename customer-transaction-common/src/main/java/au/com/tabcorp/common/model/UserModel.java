package au.com.tabcorp.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data()
@AllArgsConstructor()
@NoArgsConstructor()
@Builder()
@EqualsAndHashCode()
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserModel implements Serializable {
    private String username;
    private String password;
    private List<String> roles;
}

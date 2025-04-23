package au.com.tabcorp.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data()
@AllArgsConstructor()
@NoArgsConstructor()
@Builder()
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericErrorModel extends StandardErrorModel {
    private String details;
}

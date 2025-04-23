package au.com.tabcorp.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data()
@AllArgsConstructor()
@NoArgsConstructor()
@Builder()
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocationTransactionNumberModel implements Serializable {
    private String location;
    private Long transactions;
}

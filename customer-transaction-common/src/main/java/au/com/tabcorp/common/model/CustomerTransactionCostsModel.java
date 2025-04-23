package au.com.tabcorp.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Data()
@AllArgsConstructor()
@NoArgsConstructor()
@Builder()
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerTransactionCostsModel implements Serializable {
    private String firstName;
    private String lastName;
    private Long id;
    private BigDecimal costs;
}

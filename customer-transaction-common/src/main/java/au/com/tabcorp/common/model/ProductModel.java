package au.com.tabcorp.common.model;

import au.com.tabcorp.common.enums.ProductStatus;
import lombok.*;

import java.math.BigDecimal;

@Setter()
@Getter()
@NoArgsConstructor()
@AllArgsConstructor()
@Builder
@EqualsAndHashCode
public class ProductModel implements AbstractModel {
    public static final int MAXIMUM_LENGTH_PRODUCT_CODE = 16;
    private String code;
    private BigDecimal cost;
    private ProductStatus status;
}

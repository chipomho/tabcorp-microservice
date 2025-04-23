package au.com.tabcorp.data.entity;

import au.com.tabcorp.common.enums.ProductStatus;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder()
@NoArgsConstructor
@AllArgsConstructor
@Getter()
@Setter()
@Table(name = "product")
public class Product implements Serializable {

    @Id()
    @Column("CODE")
    private String code;

    @Column("PRODUCT_COST")
    private BigDecimal cost;

    @Column("PRODUCT_STATUS")
    private ProductStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        return new EqualsBuilder().append(getCode(), ((Product)o).getCode()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getCode()).toHashCode();
    }

}

package au.com.tabcorp.data.entity;

import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder()
@NoArgsConstructor
@AllArgsConstructor
@Getter()
@Setter()
@Table(name = "customer_transaction")
public class CustomerTransaction implements Serializable {

    @Id()
    @Column("ID")
    private Long id;

    @Column("TRANSACTION_DATETIME")
    private LocalDateTime transactionTime;

    @Column("CUSTOMER_ID")
    private Long customerId;

    @Column("PRODUCT_QUANTITY")
    private int quantity;

    @Column("PRODUCT_CODE")
    private String productCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerTransaction)) return false;
        final CustomerTransaction other = (CustomerTransaction) o;
        return new EqualsBuilder().append(getCustomerId(), other.getCustomerId())
                .append(getQuantity(), other.getQuantity())
                .append(getProductCode(), other.getProductCode())
                .append(getTransactionTime(), other.getTransactionTime()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getId()).append(getCustomerId()).append(getProductCode()).toHashCode();
    }

}

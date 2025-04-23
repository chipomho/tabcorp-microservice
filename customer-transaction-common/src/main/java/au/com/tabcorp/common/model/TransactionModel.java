package au.com.tabcorp.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDateTime;

@Setter()
@Getter()
@NoArgsConstructor()
@AllArgsConstructor()
@Builder()

public class TransactionModel implements AbstractModel {
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime transactionTime;

    private Long customerId;
    private Integer quantity;
    private String productCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransactionModel)) return false;
        final TransactionModel other = (TransactionModel) o;
        return new EqualsBuilder().append(getCustomerId(), other.getCustomerId())
                .append(getQuantity(), other.getQuantity())
                .append(getProductCode(), other.getProductCode())
                .append(LocalDateTime.of(transactionTime.getYear(),transactionTime.getMonth(), transactionTime.getDayOfMonth(), transactionTime.getHour(),transactionTime.getMinute(), transactionTime.getSecond()),
                        LocalDateTime.of(other.transactionTime.getYear(),other.transactionTime.getMonth(), other.transactionTime.getDayOfMonth(), other.transactionTime.getHour(),other.transactionTime.getMinute(), other.transactionTime.getSecond())).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getId()).append(getCustomerId()).append(getProductCode()).toHashCode();
    }

}

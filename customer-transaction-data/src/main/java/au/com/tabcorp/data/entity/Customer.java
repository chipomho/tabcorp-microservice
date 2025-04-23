package au.com.tabcorp.data.entity;

import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@Builder()
@NoArgsConstructor
@AllArgsConstructor
@Getter()
@Setter()
@Table(name = "customer")
public class Customer implements Serializable {

    @Id()
    @Column("ID")
    private Long id;

    @Column("FIRST_NAME")
    private String firstName;

    @Column("LAST_NAME")
    private String lastName;

    @Column("EMAIL_ADDRESS")
    private String emailAddress;

    @Column("LOCATION")
    private String location;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        return new EqualsBuilder().append(getEmailAddress(), ((Customer)o).getEmailAddress()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getId()).append(getEmailAddress()).toHashCode();
    }

}

package au.com.tabcorp.common.model;

import lombok.*;

@Setter()
@Getter()
@NoArgsConstructor()
@AllArgsConstructor()
@Builder()
@ToString()
@EqualsAndHashCode()
public class CustomerModel implements AbstractModel{

  public static final int MAXIMUM_LENGTH_CUSTOMER_FIRST_NAME = 120;
  public static final int MAXIMUM_LENGTH_CUSTOMER_LAST_NAME = 120;
  public static final int MAXIMUM_LENGTH_CUSTOMER_EMAIL_ADDRESS = 120;
  public static final int MAXIMUM_LENGTH_CUSTOMER_LOCATION = 80;

  private Long id;
  private String firstName;
  private String lastName;
  private String emailAddress;
  private String location;
}

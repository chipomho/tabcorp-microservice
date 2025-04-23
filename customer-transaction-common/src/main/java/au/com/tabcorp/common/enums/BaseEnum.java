package au.com.tabcorp.common.enums;

import java.io.Serializable;

public interface BaseEnum extends Serializable {

    String getCode();

    String getDescription();
}

package au.com.tabcorp.data.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

@Getter()
@Setter()
@ConfigurationProperties(ignoreUnknownFields = true, prefix = "tabcorp.app.r2dbc.data" )
public class R2DBCConfigurationProperties implements Serializable {

  private String url;
  private String user;
  private String password;

}


package kafdrop.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;


@Configuration
public class SchemaRegistryConfiguration {
  @Component
  @ConfigurationProperties(prefix = "schemaregistry")
  public static final class SchemaRegistryProperties {
    private String auth;
    private URL connectURL;

    public String getConnect() {
      return connectURL.toString();
    }

    public void setConnect(String connect) throws MalformedURLException {
      this.connectURL = new URL(connect);
    }

    public String getHost() {
      return connectURL.getHost();
    }

    public int getPort() {
      return connectURL.getPort();
    }

    public String getAuth() { return auth; }

    public void setAuth(String auth) { this.auth = auth; }
  }
}

package kafdrop.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;


@Configuration
public class KafkaConnectConfiguration {
    @Component
    @ConfigurationProperties(prefix = "kafkaconnect")
    public static final class KafkaConnectProperties {
        private URL connectURL;

        public String getConnect() {
            return connectURL.getHost();
        }

        public void setConnect(String connect) throws MalformedURLException {
            this.connectURL = new URL(connect);
        }

        public int getPort() {
            return connectURL.getPort();
        }
    }
}

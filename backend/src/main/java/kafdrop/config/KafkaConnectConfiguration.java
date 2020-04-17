package kafdrop.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class KafkaConnectConfiguration {
    @Component
    @ConfigurationProperties(prefix = "kafkaconnect")
    public static final class KafkaConnectProperties {
        private String connect;

        private int connectPort = 80;

        public String getConnect() {
            return connect;
        }

        public void setConnect(String connect) {
            this.connect = connect;
        }

        public int getConnectPort() {
            return connectPort;
        }

        public void setConnectPort(int connectPort) {
            this.connectPort = connectPort;
        }
    }
}

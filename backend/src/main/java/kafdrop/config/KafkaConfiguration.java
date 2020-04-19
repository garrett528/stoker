package kafdrop.config;

import java.io.*;
import java.util.*;

import kafdrop.exception.KafkaConfigurationException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.*;
import org.apache.kafka.common.config.*;
import org.springframework.boot.context.properties.*;
import org.springframework.stereotype.*;


@Slf4j
@Component
@ConfigurationProperties(prefix = "kafka")
@Data
public final class KafkaConfiguration {
  private String brokerConnect;
  private Boolean isSecured = false;
  private String saslMechanism;
  private String securityProtocol;
  private String truststoreFile;
  private String propertiesFile;
  private String keystoreFile;

  public void applyCommon(Properties properties) {
    properties.setProperty(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, brokerConnect);
    if (isSecured) {
      log.warn("The 'isSecured' property is deprecated; consult README.md on the preferred way to configure security");
      properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, securityProtocol);
      properties.put(SaslConfigs.SASL_MECHANISM, saslMechanism);
    }

    log.info("Checking truststore file {}", truststoreFile);
    if (new File(truststoreFile).isFile()) {
      log.info("Assigning truststore location to {}", truststoreFile);
      properties.put("ssl.truststore.location", truststoreFile);
    }

    log.info("Checking keystore file {}", keystoreFile);
    if (new File(keystoreFile).isFile()) {
      log.info("Assigning keystore location to {}", keystoreFile);
      properties.put("ssl.keystore.location", keystoreFile);
    }

    log.info("Checking properties file {}", propertiesFile);
    final var propertiesFile = new File(this.propertiesFile);
    if (propertiesFile.isFile()) {
      log.info("Loading properties from {}", this.propertiesFile);
      final var propertyOverrides = new Properties();
      try (var propsReader = new BufferedReader(new FileReader(propertiesFile))) {
        propertyOverrides.load(propsReader);
      } catch (IOException e) {
        throw new KafkaConfigurationException(e);
      }
      properties.putAll(propertyOverrides);
    }
  }
}
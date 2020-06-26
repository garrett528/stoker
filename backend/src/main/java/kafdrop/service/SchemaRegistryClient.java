package kafdrop.service;

import com.google.gson.JsonParser;
import kafdrop.config.SchemaRegistryConfiguration;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.annotation.PostConstruct;

public class SchemaRegistryClient {
    private final SchemaRegistryConfiguration.SchemaRegistryProperties schemaRegistryConfiguration;

    private final PoolingHttpClientConnectionManager cm;

    private final JsonParser jsonParser;

    private HttpHost connectHost;

    public SchemaRegistryClient(SchemaRegistryConfiguration.SchemaRegistryProperties schemaRegistryConfiguration) {
        this.schemaRegistryConfiguration = schemaRegistryConfiguration;
        this.cm = new PoolingHttpClientConnectionManager();
        this.jsonParser = new JsonParser();
    }

    private CloseableHttpClient createClient() {
        return HttpClients.custom().setConnectionManager(cm).build();
    }

    @PostConstruct
    private void createHost() {
        if (schemaRegistryConfiguration.getConnect() != null) {
            connectHost = new HttpHost(schemaRegistryConfiguration.getHost(), schemaRegistryConfiguration.getPort());
        }
    }
}

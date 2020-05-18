package kafdrop.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import kafdrop.config.KafkaConnectConfiguration;
import kafdrop.exception.KafkaAdminClientException;
import kafdrop.exception.KafkaConnectClientException;
import kafdrop.model.ConnectorStatusVO;
import kafdrop.model.ConnectorVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@Service
public class KafkaConnectClient {
    private final KafkaConnectConfiguration.KafkaConnectProperties kafkaConnectProperties;

    private final PoolingHttpClientConnectionManager cm;

    private final JsonParser jsonParser;

    private HttpHost connectHost;

    public KafkaConnectClient(KafkaConnectConfiguration.KafkaConnectProperties kafkaConnectProperties) {
        this.kafkaConnectProperties = kafkaConnectProperties;
        this.cm = new PoolingHttpClientConnectionManager();
        this.jsonParser = new JsonParser();
    }

    private CloseableHttpClient createClient() {
        return HttpClients.custom().setConnectionManager(cm).build();
    }

    @PostConstruct
    private void createHost() {
        if (kafkaConnectProperties.getConnect() != null) {
            connectHost = new HttpHost(kafkaConnectProperties.getConnect(), kafkaConnectProperties.getConnectPort());
        }
    }

    public JsonArray getAllConnectors() {
        if (kafkaConnectProperties.getConnect() == null) {
            return new JsonArray();
        }

        final var httpClient = createClient();
        log.info("Connect host URI: " + connectHost.toURI());
        final var getConnectors = new HttpGet(connectHost.toURI() + "/connectors");
        try {
            var response = httpClient.execute(getConnectors);
            final var connectorList = jsonParser.parse(EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8)).getAsJsonArray();
            log.info("Connector list: " + connectorList.toString());
            return connectorList;
        } catch (IOException e) {
            log.error("Failed to fetch connectors!");
            throw new KafkaConnectClientException(e);
        }
    }

    public ConnectorVO getConnector(String name) {
        DefaultHttpRequestRetryHandler retryhandler = new DefaultHttpRequestRetryHandler(10, true);
        final var httpClient = HttpClientBuilder.create().setRetryHandler(retryhandler).build();
        final var getConnector = new HttpGet(connectHost.toURI() + "/connectors/" + name);
        CloseableHttpResponse res;

        try {
            System.out.println("Getting connector information for: " + name);
            res = httpClient.execute(getConnector);
            var connector = jsonParser.parse(EntityUtils.toString(res.getEntity(), StandardCharsets.UTF_8)).getAsJsonObject();
            var connectorName = connector.get("name").getAsString();
            var configJson = connector.get("config").getAsJsonObject();
            var tasks = connector.get("tasks").getAsJsonArray();
            return new ConnectorVO(connectorName, configJson, tasks);
        } catch (Exception e) {
            log.error("Failed to fetch connector!");
            throw new KafkaAdminClientException(e);
        }
    }

    public BasicHttpResponse restartTask(String name, int taskId) {
        final var httpClient = createClient();
        final var restartTask = new HttpPost(connectHost.toURI() + "/connectors/" + name + "/tasks/" + taskId + "/restart");

        try {
            var res = httpClient.execute(restartTask);
            return new BasicHttpResponse(res.getStatusLine());
        } catch (IOException e) {
            log.error("Failed to fetch connector!");
            throw new KafkaConnectClientException(e);
        }
    }

    public BasicHttpResponse upsertConfiguration(String name, String configJson) {
        final var httpClient = createClient();
        try {
            var upsertConfiguration = new HttpPut(connectHost.toURI() + "/connectors/" + name + "/config");
            upsertConfiguration.setHeader(HttpHeaders.ACCEPT, "application/json");
            upsertConfiguration.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            upsertConfiguration.setEntity(new StringEntity(configJson));
            log.info("Upserting configuration for connector: " + name);
            var res = httpClient.execute(upsertConfiguration);
            System.out.println(EntityUtils.toString(res.getEntity()));
            return new BasicHttpResponse(res.getStatusLine());
        } catch (IOException e) {
            log.error("Failed to upsert configuration!");
            throw new KafkaConnectClientException(e);
        }
    }

    public ConnectorStatusVO getConnectorStatus(String name) {
        final var httpClient = createClient();
        final var getConnectorStatus = new HttpGet(connectHost.toURI() + "/connectors/" + name + "/status");
        try {
            var res = httpClient.execute(getConnectorStatus);
            var connectorStatus = jsonParser.parse(EntityUtils.toString(res.getEntity(), StandardCharsets.UTF_8)).getAsJsonObject();
            var connectorStatusVO = new ConnectorStatusVO(connectorStatus.get("name").getAsString(), connectorStatus.get("connector").getAsJsonObject().get("state").getAsString());
            var tasks = connectorStatus.get("tasks").getAsJsonArray();

            for (JsonElement task : tasks) {
                parseConnectorTask(connectorStatusVO, task);
            }

            return connectorStatusVO;
        } catch (IOException e) {
            log.error("Failed to fetch connector status!");
            throw new KafkaConnectClientException(e);
        }
    }

    private void parseConnectorTask(ConnectorStatusVO connectorStatusVO, JsonElement task) {
        var taskJson = task.getAsJsonObject();
        var id = taskJson.get("id").getAsInt();
        var state = taskJson.get("state").getAsString();
        var workerId = taskJson.get("worker_id").getAsString();
        Optional<String> trace;

        if (taskJson.has("trace")) {
            trace = Optional.ofNullable(taskJson.get("trace").getAsString());
        } else {
            trace = Optional.empty();
        }

        connectorStatusVO.addConnectorTask(id, state, workerId, trace);
    }
}

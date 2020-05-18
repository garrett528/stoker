package kafdrop.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kafdrop.model.ConnectorStatusVO;
import kafdrop.service.KafkaConnectClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/connect")
public class ConnectController {
    private final KafkaConnectClient kafkaConnectClient;

    private final Gson gson = new Gson();

    public ConnectController(KafkaConnectClient kafkaConnectClient) {
        this.kafkaConnectClient = kafkaConnectClient;
    }

    @ApiOperation(value = "getAllConnectors", notes = "Get JsonArray of all connectors")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class)
    })
    @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllConnectors() {
        return ResponseEntity.ok(gson.toJson(kafkaConnectClient.getAllConnectors()));
    }

    @ApiOperation(value = "getConnector", notes = "Get details for a connector")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "Connector not found")
    })
    @GetMapping(path = "/{name:.+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getConnector(@PathVariable("name") String connectorName) {
        return ResponseEntity.ok(gson.toJson(kafkaConnectClient.getConnector(connectorName)));
    }

    @ApiOperation(value = "getConnectorStatuses", notes = "Get all connectors and their statuses")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Collection.class)
    })
    @GetMapping(path = "/connectorStatuses", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<ConnectorStatusVO> getConnectorStatuses() {
        var connectStatuses = new ArrayList<ConnectorStatusVO>();
        final var connectorList = kafkaConnectClient.getAllConnectors();

        for (JsonElement connector : connectorList) {
            final var connectSummary = kafkaConnectClient.getConnectorStatus(connector.getAsString());
            connectStatuses.add(connectSummary);
        }
        return connectStatuses;
    }

    @ApiOperation(value = "getConnectorStatus", notes = "Get a specific connector status")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ConnectorStatusVO.class)
    })
    @GetMapping(path = "/{name:.+}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ConnectorStatusVO getConnectorStatus(@PathVariable("name") String connectorName) {
        return kafkaConnectClient.getConnectorStatus(connectorName);
    }

    @ApiOperation(value = "restartConnectorTask", notes = "Restarts a specific task for a connector")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class)
    })
    @PostMapping(path = "/restartTask/{name:.+}/{task:.+}")
    public ResponseEntity<String> restartTask(@PathVariable("name") String connectorName, @PathVariable("task") int taskId) {
        final var response = kafkaConnectClient.restartTask(connectorName, taskId);
        final var statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 204) {
            return ResponseEntity.status(statusCode).body("restart task failed");
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "upsertConfiguration", notes = "Changes the configuration of a connector")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class)
    })
    @PostMapping(path = "/upsertConfiguration/{name:.+}")
    public ResponseEntity<String> upsertConfiguration(@PathVariable("name") String connectorName, @RequestBody String configJson) {
        final var configURLDecoded = URLDecoder.decode(configJson, StandardCharsets.UTF_8);
        System.out.println(configURLDecoded);
        final var response = kafkaConnectClient.upsertConfiguration(connectorName, configURLDecoded);
        final var statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        if (statusCode != 201 && statusCode != 200) {
            return ResponseEntity.status(statusCode).body("error upserting configuration");
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

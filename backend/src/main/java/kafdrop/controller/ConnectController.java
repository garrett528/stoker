package kafdrop.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kafdrop.model.ConnectorVO;
import kafdrop.service.KafkaConnectClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequestMapping("/connect")
public class ConnectController {
    private final KafkaConnectClient kafkaConnectClient;
    private final Gson gson;

    public ConnectController(KafkaConnectClient kafkaConnectClient) {
        this.kafkaConnectClient = kafkaConnectClient;
        this.gson = new Gson();
    }

    @ApiOperation(value = "getConnector", notes = "Get details for a connector")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ConnectorVO.class),
            @ApiResponse(code = 404, message = "Connector not found")
    })
    @GetMapping(path = "/{name:.+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getConnector(@PathVariable("name") String connectorName) {
        return ResponseEntity.ok(gson.toJson(kafkaConnectClient.getConnector(connectorName)));
    }

    @ApiOperation(value = "getAllConnectors", notes = "Get JsonArray of all connectors")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = JsonArray.class)
    })
    @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllConnectors() {
        return ResponseEntity.ok(gson.toJson(kafkaConnectClient.getAllConnectors()));
    }

    @ApiOperation(value = "restartConnectorTask", notes = "Restarts a specific task for a connector")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
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
            @ApiResponse(code = 200, message = "Success")
    })
    @PostMapping(path = "/upsertConfiguration/{name:.+}")
    public ResponseEntity<String> upsertConfiguration(@PathVariable("name") String connectorName, @RequestBody String configJson) {
        final var configURLDecoded = URLDecoder.decode(configJson, StandardCharsets.UTF_8);
        final var configDecoded = Base64.getDecoder().decode(configURLDecoded);
        final var response = kafkaConnectClient.upsertConfiguration(connectorName, new String(configDecoded));
        final var statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        if (statusCode != 201 && statusCode != 200) {
            return ResponseEntity.status(statusCode).body("error upserting configuration");
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

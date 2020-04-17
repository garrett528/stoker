package kafdrop.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kafdrop.model.ConnectorVO;
import kafdrop.service.KafkaConnectClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Controller
@RequestMapping("/connect")
public class ConnectController {
    private final KafkaConnectClient kafkaConnectClient;
    private final Gson gson;

    public ConnectController(KafkaConnectClient kafkaConnectClient) {
        this.kafkaConnectClient = kafkaConnectClient;
        this.gson = new Gson();
    }

    @RequestMapping("/{name:.+}")
    public String topicDetails(@PathVariable("name") String connectorName, Model model) {
        final var connector = kafkaConnectClient.getConnector(connectorName);
        model.addAttribute("connector", connector);

        final var connectorStatus = kafkaConnectClient.getConnectorStatus(connectorName);
        model.addAttribute("connectorStatus", connectorStatus);

        return "connector-detail";
    }

    @RequestMapping(path = "/restartTask/{name:.+}/{task:.+}")
    public ModelAndView restartTask(@PathVariable("name") String connectorName, @PathVariable("task") int taskId) {
        final var response = kafkaConnectClient.restartTask(connectorName, taskId);
        final var statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 204) {
            throw new RuntimeException("Restart task " + taskId + " for connector " + connectorName + " failed with " +
                    "status " + statusCode);
        }

        return new ModelAndView("redirect:/connect/" + connectorName);
    }

    @RequestMapping(path = "/upsertConfiguration/{name:.+}", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> upsertConfiguration(@PathVariable("name") String connectorName, @RequestBody String configJson) {
        final var configURLDecoded = URLDecoder.decode(configJson, StandardCharsets.UTF_8);
        final var configDecoded = Base64.getDecoder().decode(configURLDecoded);
        final var response = kafkaConnectClient.upsertConfiguration(connectorName, new String(configDecoded));
        final var statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        if (statusCode != 201 && statusCode != 200) {
            throw new RuntimeException("Upsert configuration for connector " + connectorName + " failed with " +
                    "status " + statusCode);
        }

        System.out.println(gson.toJson("Upsert configuration success"));
        return ResponseEntity.ok(gson.toJson("Success"));
    }

    @ApiOperation(value = "getConnector", notes = "Get details for a connector")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ConnectorVO.class),
            @ApiResponse(code = 404, message = "Connector not found")
    })
    @RequestMapping(path = "/{name:.+}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<String> getConnector(@PathVariable("name") String connectorName) {
        return ResponseEntity.ok(gson.toJson(kafkaConnectClient.getConnector(connectorName)));
    }

    @ApiOperation(value = "getAllConnectors", notes = "Get JsonArray of all connectors")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = JsonArray.class)
    })
    @RequestMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<String> getAllConnectors() {
        return ResponseEntity.ok(gson.toJson(kafkaConnectClient.getAllConnectors()));
    }
}

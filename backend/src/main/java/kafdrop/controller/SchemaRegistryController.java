package kafdrop.controller;

import com.google.gson.Gson;
import kafdrop.service.SchemaRegistryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schema-registry")
public class SchemaRegistryController {
    private final SchemaRegistryClient schemaRegistryClient;

    private final Gson gson = new Gson();

    public SchemaRegistryController(SchemaRegistryClient schemaRegistryClient) {
        this.schemaRegistryClient = schemaRegistryClient;
    }
}

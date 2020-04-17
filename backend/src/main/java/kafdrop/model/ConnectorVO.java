package kafdrop.model;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Data;

@Data
public class ConnectorVO {
    private final String name;

    private final JsonObject configJson;

    private final JsonArray tasks;

    public String prettyPrintConfig(JsonObject config) {
        var gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(config);
    }
}

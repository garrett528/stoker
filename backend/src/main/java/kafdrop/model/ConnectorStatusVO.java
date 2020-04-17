package kafdrop.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class ConnectorStatusVO {
    private final String name;
    private final String status;
    private final List<ConnectorTask> taskList;

    public ConnectorStatusVO(String name, String status) {
        this.name = name;
        this.status = status;
        this.taskList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public List<ConnectorTask> getTaskList() {
        return taskList;
    }

    public void addConnectorTask(int id, String state, String workerId, Optional<String> trace) {
        var task = new ConnectorTask(id, state, workerId, trace);
        taskList.add(task);
    }

    @Data
    public final class ConnectorTask {
        private final int id;
        private final String state;
        private final String workerId;
        private final Optional<String> trace;
    }
}

package ch.unisg.tapascommon.tasks.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import java.util.UUID;

@EqualsAndHashCode
public class Task {
    public enum Status {
        OPEN, ASSIGNED, RUNNING, EXECUTED
    }

    public enum Type {
        UNKNOWN, BIGROBOT, SMALLROBOT, COMPUTATION, RANDOMTEXT, TEMPERATURE, HUMIDITY
    }

    @Getter
    private final TaskId taskId;

    @Getter
    private final TaskName taskName;

    @Getter
    private final TaskType taskType;

    @Getter
    private final OriginalTaskUri originalTaskUri;

    @Getter @Setter
    private TaskStatus taskStatus;

    @Getter @Setter
    private ServiceProvider provider;

    @Getter @Setter
    private InputData inputData;

    @Getter @Setter
    private OutputData outputData;

    public Task(
            TaskId taskId,
            TaskName taskName,
            TaskType taskType,
            OriginalTaskUri taskUri,
            TaskStatus taskStatus,
            ServiceProvider provider,
            InputData inputData,
            OutputData outputData
    ) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskType = taskType;
        this.originalTaskUri = taskUri;
        this.taskStatus = taskStatus;
        this.provider = provider;
        this.inputData = inputData;
        this.outputData = outputData;
    }

    public static Task createTaskWithNameAndTypeAndOriginalTaskUri(
            Task.TaskName taskName,
            Task.TaskType taskType,
            Task.OriginalTaskUri originalTaskUri
    ) {
        return new Task(
                new Task.TaskId(UUID.randomUUID().toString()),
                taskName,
                taskType,
                originalTaskUri,
                new Task.TaskStatus(Task.Status.OPEN),
                new Task.ServiceProvider(""),
                new Task.InputData(""),
                new Task.OutputData("")
        );
    }

    public static Task createTaskWithNameAndType(
            Task.TaskName taskName,
            Task.TaskType taskType
    ) {
        return createTaskWithNameAndTypeAndOriginalTaskUri(
                taskName,
                taskType,
                new Task.OriginalTaskUri("")
        );
    }

    public Type getTaskTypeEnum() {
        try {
            return Type.valueOf(taskType.value.toUpperCase());
        } catch(IllegalArgumentException e) {
            return Type.UNKNOWN;
        }
    }

    @Value
    public static class TaskId {
        String value;
    }

    @Value
    public static class TaskName {
        String value;
    }

    @Value
    public static class TaskType {
        String value;
    }

    @Value
    public static class OriginalTaskUri {
        String value;
    }

    @Value
    public static class TaskStatus {
        Status value;
    }

    @Value
    public static class ServiceProvider {
        String value;
    }

    @Value
    public static class InputData {
        String value;
    }

    @Value
    public static class OutputData {
        String value;
    }

    @Override
    public String toString() {
        var originalTaskUriString = originalTaskUri != null ? originalTaskUri.getValue() : "";
        var providerString = provider != null ? provider.getValue() : "";
        var inputDataString = inputData !=  null ? inputData.getValue() : "";
        var outputDataString = outputData != null ? outputData.getValue() : "";

        return "Task{" +
                "taskId=" + taskId.getValue() +
                ", taskName=" + taskName.getValue() +
                ", taskType=" + taskType.getValue() +
                ", originalTaskUri=" + originalTaskUriString +
                ", taskStatus=" + taskStatus.getValue().name() +
                ", provider=" + providerString +
                ", inputData=" + inputDataString +
                ", outputData=" + outputDataString +
                '}';
    }
}

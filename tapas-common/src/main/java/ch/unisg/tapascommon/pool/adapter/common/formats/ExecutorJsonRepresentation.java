package ch.unisg.tapascommon.pool.adapter.common.formats;

import ch.unisg.tapascommon.tasks.domain.Task;
import ch.unisg.tapascommon.pool.domain.Executor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExecutorJsonRepresentation {
    public static final String MEDIA_TYPE = "application/executor+json";

    private String executorId;
    private String executorName;
    private String executorType;
    private String executorAddress;
    private String executorState;
    private String executorPoolName;

    public ExecutorJsonRepresentation() { }

    public ExecutorJsonRepresentation(
            String executorId,
            String executorName,
            String executorType,
            String executorAddress
    ) {
        this.executorId = executorId;
        this.executorName = executorName;
        this.executorType = executorType;
        this.executorAddress = executorAddress;
    }

    public ExecutorJsonRepresentation(Executor executor) {
        this.executorId = executor.getExecutorId().getValue();
        this.executorName = executor.getExecutorName().getValue();
        this.executorType = executor.getExecutorType().getValue().name();
        this.executorAddress = executor.getExecutorAddress().getValue();
        this.executorState = executor.getExecutorState().getValue().name();
        this.executorPoolName = executor.getExecutorPoolName().getValue();
    }

    public static String serialize(Executor executor) throws JsonProcessingException {
        var representation = new ExecutorJsonRepresentation(executor);
        return representation.serialize();
    }

    public String serialize() throws JsonProcessingException {
        var mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsString(this);
    }

    public static Executor deserialize(ExecutorJsonRepresentation representation) {
        return new Executor(
                new Executor.ExecutorId(representation.executorId),
                new Executor.ExecutorName(representation.executorName),
                new Executor.ExecutorType(Task.Type.valueOf(representation.executorType)),
                new Executor.ExecutorAddress(representation.executorAddress),
                new Executor.ExecutorState(Executor.State.valueOf(representation.executorState)),
                new Executor.ExecutorPoolName(representation.getExecutorPoolName())
        );
    }

    public Executor deserialize() {
        return deserialize(this);
    }

    public static ExecutorJsonRepresentation fromJsonString(String json) throws JsonProcessingException {
        var data = new ObjectMapper().readTree(json);
        return new ExecutorJsonRepresentation(
                data.get("executorId").textValue(),
                data.get("executorName").textValue(),
                data.get("executorType").textValue(),
                data.get("executorAddress").textValue(),
                data.get("executorState").textValue(),
                data.get("executorPoolName").textValue()
        );
    }
}

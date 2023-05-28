package ch.unisg.tapasexecutorbase.executor.domain;

import ch.unisg.tapascommon.common.SelfValidating;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Value
public class TaskUpdatedEvent extends SelfValidating<TaskUpdatedEvent> {
    @Getter
    @NotNull
    String taskId;

    @Getter
    @NotNull
    String taskStatus;

    @Getter
    @NotNull
    String taskUri;

    @Getter
    String outputData;

    public TaskUpdatedEvent(String taskId, String taskStatus, String outputData, String taskUri) {
        this.taskId = taskId;
        this.taskStatus = taskStatus;
        this.outputData = outputData;
        this.taskUri = taskUri;
        this.validateSelf();
    }
}

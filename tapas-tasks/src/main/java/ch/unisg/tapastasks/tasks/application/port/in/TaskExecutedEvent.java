package ch.unisg.tapastasks.tasks.application.port.in;

import ch.unisg.tapascommon.common.SelfValidating;
import ch.unisg.tapascommon.tasks.domain.Task.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
@Value
public class TaskExecutedEvent extends SelfValidating<TaskExecutedEvent> {
    @NotNull
    TaskId taskId;

    @Getter
    Optional<ServiceProvider> serviceProvider;

    @Getter
    Optional<OutputData> outputData;

    public TaskExecutedEvent(TaskId taskId, Optional<ServiceProvider> serviceProvider,
            Optional<OutputData> outputData) {
        this.taskId = taskId;

        this.serviceProvider = serviceProvider;
        this.outputData = outputData;

        this.validateSelf();
    }
}

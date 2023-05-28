package ch.unisg.tapastasks.tasks.application.port.in;

import ch.unisg.tapascommon.common.SelfValidating;
import ch.unisg.tapascommon.tasks.domain.Task;
import lombok.Getter;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Value
public class TaskAssignedEvent extends SelfValidating<TaskAssignedEvent> {
    @NotNull
    private final Task.TaskId taskId;

    @Getter
    private final Optional<Task.ServiceProvider> serviceProvider;

    public TaskAssignedEvent(Task.TaskId taskId, Optional<Task.ServiceProvider> serviceProvider) {
        this.taskId = taskId;
        this.serviceProvider = serviceProvider;

        this.validateSelf();
    }
}

package ch.unisg.tapasexecutorpool.pool.application.port.out;

import ch.unisg.tapascommon.common.SelfValidating;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = false)
@Value
public class TaskAssignedEvent extends SelfValidating<TaskAssignedEvent> {
    @Getter
    @NotNull
    String serviceProvider;

    @Getter
    @NotNull
    String taskUri;

    public TaskAssignedEvent(String serviceProvider, String taskUri) {
        this.serviceProvider = serviceProvider;
        this.taskUri = taskUri;
        this.validateSelf();
    }
}

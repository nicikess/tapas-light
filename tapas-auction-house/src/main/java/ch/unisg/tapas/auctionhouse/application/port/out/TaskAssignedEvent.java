package ch.unisg.tapas.auctionhouse.application.port.out;

import ch.unisg.tapascommon.common.SelfValidating;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
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

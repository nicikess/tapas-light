package ch.unisg.tapas.auctionhouse.application.port.in;

import ch.unisg.tapascommon.common.SelfValidating;
import ch.unisg.tapascommon.tasks.adapter.common.formats.TaskJsonRepresentation;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Value
public class TaskWonEvent extends SelfValidating<TaskWonEvent> {

    @Getter
    @NotNull
    TaskJsonRepresentation taskJsonRepresentation;

    public TaskWonEvent(TaskJsonRepresentation taskJsonRepresentation) {
        this.taskJsonRepresentation = taskJsonRepresentation;
        this.validateSelf();
    }
}

package ch.unisg.tapasroster.roster.application.port.in;

import ch.unisg.tapascommon.common.SelfValidating;
import ch.unisg.tapascommon.tasks.domain.Task;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = false)
@Value
public class ScheduleTaskCommand extends SelfValidating<ScheduleTaskCommand> {

    @Getter
    @NotNull
    Task task;

    public ScheduleTaskCommand(Task task) {
        this.task = task;
        this.validateSelf();
    }
}

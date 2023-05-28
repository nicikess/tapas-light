package ch.unisg.tapasexecutorpool.pool.application.port.in;

import ch.unisg.tapascommon.common.SelfValidating;
import ch.unisg.tapascommon.tasks.domain.Task;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = false)
@Value
public class ExecuteTaskCommand extends SelfValidating<ExecuteTaskCommand> {
    @NotNull
    Task task;

    public ExecuteTaskCommand(Task task) {
        this.task = task;
        this.validateSelf();
    }
}

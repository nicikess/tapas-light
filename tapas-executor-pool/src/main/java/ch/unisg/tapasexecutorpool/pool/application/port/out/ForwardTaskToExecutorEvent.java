package ch.unisg.tapasexecutorpool.pool.application.port.out;

import ch.unisg.tapascommon.common.SelfValidating;
import ch.unisg.tapascommon.pool.domain.Executor;
import ch.unisg.tapascommon.tasks.domain.Task;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = false)
@Value
public class ForwardTaskToExecutorEvent extends SelfValidating<ForwardTaskToExecutorEvent> {

    @Getter
    @NotNull
    Task task;

    @Getter
    @NotNull
    Executor executor;

    public ForwardTaskToExecutorEvent(Task task, Executor executor) {
        this.task = task;
        this.executor = executor;
        this.validateSelf();
    }
}

package ch.unisg.tapasexecutorpool.pool.application.port.in;

import ch.unisg.tapascommon.common.SelfValidating;
import ch.unisg.tapascommon.pool.domain.Executor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = false)
@Value
public class UpdateExecutorStateCommand extends SelfValidating<UpdateExecutorStateCommand> {

    @NotNull
    Executor.ExecutorId executorId;

    @NotNull
    Executor.ExecutorState executorState;

    public UpdateExecutorStateCommand(
            Executor.ExecutorId executorId,
            Executor.ExecutorState executorState
    ) {
        this.executorId = executorId;
        this.executorState = executorState;

        this.validateSelf();
    }
}

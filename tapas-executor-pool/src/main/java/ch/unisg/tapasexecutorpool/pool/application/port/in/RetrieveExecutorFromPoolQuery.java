package ch.unisg.tapasexecutorpool.pool.application.port.in;

import ch.unisg.tapascommon.common.SelfValidating;
import ch.unisg.tapascommon.pool.domain.Executor.ExecutorId;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = false)
@Value
public class RetrieveExecutorFromPoolQuery extends SelfValidating<RetrieveExecutorFromPoolQuery> {
    @NotNull
    ExecutorId executorId;

    public RetrieveExecutorFromPoolQuery(ExecutorId executorId) {
        this.executorId = executorId;
        this.validateSelf();
    }
}

package ch.unisg.tapasexecutorpool.pool.adapter.out.persistence.mongodb;

import ch.unisg.tapascommon.pool.domain.Executor;
import ch.unisg.tapascommon.tasks.domain.Task;
import org.springframework.stereotype.Component;

@Component
class ExecutorMapper {
    Executor mapToDomainEntity(ExecutorMongoDocument executor) {
        return new Executor(
                new Executor.ExecutorId(executor.getExecutorId()),
                new Executor.ExecutorName(executor.getExecutorName()),
                new Executor.ExecutorType(Task.Type.valueOf(executor.getExecutorType())),
                new Executor.ExecutorAddress(executor.getExecutorAddress()),
                new Executor.ExecutorState(Executor.State.valueOf(executor.getExecutorState())),
                new Executor.ExecutorPoolName(executor.getExecutorPoolName())
        );
    }

    ExecutorMongoDocument mapToMongoDocument(Executor executor) {
        return new ExecutorMongoDocument(
                executor.getExecutorId().getValue(),
                executor.getExecutorName().getValue(),
                executor.getExecutorType().getValue().name(),
                executor.getExecutorAddress().getValue(),
                executor.getExecutorState().getValue().name(),
                executor.getExecutorPoolName().getValue()
        );
    }
}

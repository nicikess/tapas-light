package ch.unisg.tapasexecutorpool.pool.application.service;

import ch.unisg.tapascommon.tasks.domain.Task;
import ch.unisg.tapasexecutorpool.pool.application.port.in.AddNewExecutorToPoolCommand;
import ch.unisg.tapasexecutorpool.pool.application.port.in.AddNewExecutorToPoolUseCase;
import ch.unisg.tapascommon.pool.domain.Executor;
import ch.unisg.tapasexecutorpool.pool.application.port.out.AddExecutorToRepositoryPort;
import ch.unisg.tapasexecutorpool.pool.application.port.out.ExecutorPoolLock;
import ch.unisg.tapasexecutorpool.pool.domain.ExecutorPool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Component
@Transactional
public class AddNewExecutorToPoolService implements AddNewExecutorToPoolUseCase {

    private final AddExecutorToRepositoryPort addExecutorToRepositoryPort;
    private final ExecutorPoolLock executorPoolLock;

    public Executor addNewExecutorToPool(AddNewExecutorToPoolCommand command) {
        var executorPool = ExecutorPool.getTapasExecutorPool();

        executorPoolLock.lockExecutorPool(executorPool.getExecutorPoolName());

        var newExecutor = executorPool.addNewExecutor(
                new Executor.ExecutorId(command.getExecutorId()),
                new Executor.ExecutorName(command.getExecutorName()),
                new Executor.ExecutorType(Task.Type.valueOf(command.getExecutorType().toUpperCase())),
                new Executor.ExecutorAddress(command.getExecutorAddress())
        );

        addExecutorToRepositoryPort.addExecutor(newExecutor);
        executorPoolLock.releaseExecutorPool(executorPool.getExecutorPoolName());

        return newExecutor;
    }
}

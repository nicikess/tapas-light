package ch.unisg.tapasexecutorpool.pool.application.service;

import ch.unisg.tapasexecutorpool.pool.application.port.in.UpdateExecutorStateCommand;
import ch.unisg.tapasexecutorpool.pool.application.port.in.UpdateExecutorStateUseCase;
import ch.unisg.tapasexecutorpool.pool.application.port.out.AddExecutorToRepositoryPort;
import ch.unisg.tapasexecutorpool.pool.application.port.out.ExecutorPoolLock;
import ch.unisg.tapasexecutorpool.pool.domain.ExecutorPool;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;

@RequiredArgsConstructor
@Component
@Transactional
public class UpdateExecutorStateService implements UpdateExecutorStateUseCase {

    private static final Logger LOGGER = LogManager.getLogger(UpdateExecutorStateService.class);

    private final AddExecutorToRepositoryPort addExecutorToRepositoryPort;
    private final ExecutorPoolLock executorPoolLock;

    @Override
    public boolean updateExecutorState(UpdateExecutorStateCommand command) {
        var executorPool = ExecutorPool.getTapasExecutorPool();

        var id = command.getExecutorId();
        var state = command.getExecutorState();

        var executor = executorPool.retrieveExecutorById(id);
        if (executor.isPresent()) {
            if (executorPool.updateExecutorState(command.getExecutorId(), command.getExecutorState())) {
                LOGGER.info("Update State (=" + state.getValue() + ") for Executor Id (=" + id.getValue() + ")");

                executorPoolLock.lockExecutorPool(executorPool.getExecutorPoolName());
                addExecutorToRepositoryPort.addExecutor(executor.get());
                executorPoolLock.releaseExecutorPool(executorPool.getExecutorPoolName());
                return true;
            }
        }

        return false;
    }
}

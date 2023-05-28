package ch.unisg.tapasexecutorpool.pool.application.service;

import ch.unisg.tapascommon.tasks.domain.Task;
import ch.unisg.tapasexecutorpool.pool.application.port.in.AddNewExecutorToPoolCommand;
import ch.unisg.tapasexecutorpool.pool.application.port.out.AddExecutorToRepositoryPort;
import ch.unisg.tapasexecutorpool.pool.application.port.out.ExecutorPoolLock;
import ch.unisg.tapasexecutorpool.pool.domain.ExecutorPool;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

public class AddNewExecutorToPoolServiceTest {

    private final AddExecutorToRepositoryPort addExecutorToRepositoryPort = Mockito.mock(AddExecutorToRepositoryPort.class);
    private final ExecutorPoolLock executorPoolLock = Mockito.mock(ExecutorPoolLock.class);
    private final AddNewExecutorToPoolService addNewExecutorToPoolService = new AddNewExecutorToPoolService(
            addExecutorToRepositoryPort, executorPoolLock);

    @Test
    void addingSucceeds() {

        var executorId = "test-executor-id";
        var executorName = "test-executor-name";
        var executorType = Task.Type.UNKNOWN;
        var executorAddress = "http://127.0.0.1:8085";

        var executorPool = givenAnEmptyExecutorPool();

        var addNewExecutorToPoolCommand = new AddNewExecutorToPoolCommand(
                executorId,
                executorName,
                executorType.name(),
                executorAddress
        );

        var addedTask = addNewExecutorToPoolService.addNewExecutorToPool(addNewExecutorToPoolCommand);

        assertThat(addedTask).isNotNull();
        assertThat(executorPool.getListOfExecutors().getValue()).hasSize(1);

        then(executorPoolLock).should().lockExecutorPool(eq(ExecutorPool.getTapasExecutorPool().getExecutorPoolName()));
    }

    private ExecutorPool givenAnEmptyExecutorPool() {
        var executorPool = ExecutorPool.getTapasExecutorPool();
        executorPool.getListOfExecutors().getValue().clear();
        return executorPool;
    }
}

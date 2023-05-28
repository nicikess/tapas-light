package ch.unisg.tapasexecutorpool.pool.domain;

import ch.unisg.tapascommon.pool.domain.Executor;
import ch.unisg.tapascommon.tasks.domain.Task;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
public class ExecutorPoolTest {

    @Test
    void addNewTaskToTaskListSuccess() {
        var executorPool = ExecutorPool.getTapasExecutorPool();
        executorPool.getListOfExecutors().getValue().clear();

        var executorId = "test-executor-id";
        var executorName = "test-executor-name";
        var executorType = Task.Type.UNKNOWN;
        var executorAddress = "http://127.0.0.1:8085";

        var newExecutor = executorPool.addNewExecutor(
                new Executor.ExecutorId(executorId),
                new Executor.ExecutorName(executorName),
                new Executor.ExecutorType(executorType),
                new Executor.ExecutorAddress(executorAddress)
        );

        assertThat(newExecutor.getExecutorId().getValue()).isEqualTo(executorId);
        assertThat(executorPool.getListOfExecutors().getValue()).hasSize(1);
        assertThat(executorPool.getListOfExecutors().getValue().get(0)).isEqualTo(newExecutor);
    }

    @Test
    void retrieveTaskSuccess() {
        var executorPool = ExecutorPool.getTapasExecutorPool();
        executorPool.getListOfExecutors().getValue().clear();

        var executorId = "test-executor-id";
        var executorName = "test-executor-name";
        var executorType = Task.Type.UNKNOWN;
        var executorAddress = "http://127.0.0.1:8085";

        var newExecutor = executorPool.addNewExecutor(
                new Executor.ExecutorId(executorId),
                new Executor.ExecutorName(executorName),
                new Executor.ExecutorType(executorType),
                new Executor.ExecutorAddress(executorAddress)
        );

        var retrievedExecutor = executorPool.retrieveExecutorById(newExecutor.getExecutorId()).get();

        assertThat(retrievedExecutor).isEqualTo(newExecutor);
    }

    @Test
    void retrieveTaskFailure() {
        var executorPool = ExecutorPool.getTapasExecutorPool();
        executorPool.getListOfExecutors().getValue().clear();

        var executorId = "test-executor-id";
        var executorName = "test-executor-name";
        var executorType = Task.Type.UNKNOWN;
        var executorAddress = "http://127.0.0.1:8085";

        var newExecutor = executorPool.addNewExecutor(
                new Executor.ExecutorId(executorId),
                new Executor.ExecutorName(executorName),
                new Executor.ExecutorType(executorType),
                new Executor.ExecutorAddress(executorAddress)
        );

        var fakeId = new Executor.ExecutorId("fake-id");

        var retrievedExecutor = executorPool.retrieveExecutorById(fakeId);

        assertThat(retrievedExecutor.isPresent()).isFalse();
    }
}

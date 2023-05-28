package ch.unisg.tapasexecutorpool.pool.adapter.out.persistence.mongodb;

import ch.unisg.tapascommon.pool.domain.Executor;
import ch.unisg.tapascommon.tasks.domain.Task;
import ch.unisg.tapasexecutorpool.pool.domain.ExecutorPool;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureDataMongo
@Import({ExecutorPersistenceAdapter.class, ExecutorMapper.class})
public class ExecutorPersistenceAdapterTest {

    @Autowired
    private ExecutorRepository executorRepository;

    @Autowired
    private ExecutorPersistenceAdapter adapterUnderTest;

    @Test
    void addNewExecutorToRepository() {
        var executorId = "test-executor-id";
        var executorName = "test-executor-name";
        var executorType = Task.Type.UNKNOWN;
        var executorAddress = "http://127.0.0.1:8085";
        var executorPoolName = "test-pool";

        var executorDomain = new Executor(
                new Executor.ExecutorId(executorId),
                new Executor.ExecutorName(executorName),
                new Executor.ExecutorType(executorType),
                new Executor.ExecutorAddress(executorAddress),
                new Executor.ExecutorState(Executor.State.IDLE),
                new Executor.ExecutorPoolName(executorPoolName)
        );

        adapterUnderTest.addExecutor(executorDomain);

        var executorDocument = executorRepository.findByExecutorId(executorId, executorPoolName);

        assertThat(executorDocument.getExecutorId()).isEqualTo(executorId);
        assertThat(executorDocument.getExecutorName()).isEqualTo(executorName);
        assertThat(executorDocument.getExecutorPoolName()).isEqualTo(executorPoolName);
    }

    @Test
    void retrieveExecutorFromRepository() {
        var executorId = "test-executor-id";
        var executorName = "test-executor-name";
        var executorType = Task.Type.UNKNOWN;
        var executorAddress = "http://127.0.0.1:8085";
        var executorPoolName = "test-pool";

        var executorDocument = new ExecutorMongoDocument(
                executorId,
                executorName,
                executorType.name(),
                executorAddress,
                Executor.State.IDLE.name(),
                executorPoolName
        );

        if (executorRepository.existsById(executorId)) {
            executorRepository.deleteById(executorId);
        }
        executorRepository.insert(executorDocument);

        var executorDomain = adapterUnderTest.loadExecutor(new Executor.ExecutorId(executorId), new ExecutorPool.ExecutorPoolName(executorPoolName));

        assertThat(executorDomain.getExecutorId().getValue()).isEqualTo(executorId);
        assertThat(executorDomain.getExecutorName().getValue()).isEqualTo(executorName);
        assertThat(executorDomain.getExecutorPoolName().getValue()).isEqualTo(executorPoolName);
    }
}

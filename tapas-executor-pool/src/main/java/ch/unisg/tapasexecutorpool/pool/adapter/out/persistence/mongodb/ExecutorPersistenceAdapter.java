package ch.unisg.tapasexecutorpool.pool.adapter.out.persistence.mongodb;

import ch.unisg.tapascommon.pool.domain.Executor;
import ch.unisg.tapasexecutorpool.pool.application.port.out.AddExecutorToRepositoryPort;
import ch.unisg.tapasexecutorpool.pool.application.port.out.LoadExecutorFromRepositoryPort;
import ch.unisg.tapasexecutorpool.pool.domain.ExecutorPool;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExecutorPersistenceAdapter implements AddExecutorToRepositoryPort, LoadExecutorFromRepositoryPort {

    private static final Logger LOGGER = LogManager.getLogger(ExecutorPersistenceAdapter.class);

    private final ExecutorRepository repository;
    private final ExecutorMapper mapper;

    @Override
    public void addExecutor(Executor executor) {
        try {
            var mongoExecutorDocument = mapper.mapToMongoDocument(executor);
            repository.save(mongoExecutorDocument);
            LOGGER.info("Saved Executor to MongoDB");
        } catch (Exception e) {
            LOGGER.warn("Failed to add Executor to MongoDB");
        }
    }

    @Override
    public Executor loadExecutor(Executor.ExecutorId executorId, ExecutorPool.ExecutorPoolName executorPoolName) {
        try {
            var mongoExecutorDocument = repository.findByExecutorId(executorId.getValue(),executorPoolName.getValue());
            LOGGER.info("Loaded Executor from MongoDB");
            return mapper.mapToDomainEntity(mongoExecutorDocument);
        } catch (Exception e) {
            LOGGER.warn("Failed to load Executor from MongoDB");
        }
        return null;
    }

    @Override
    public List<Executor> loadAllExecutors() {
        try {
            var mongoExecutors = repository.findAll();
            var executors = new ArrayList<Executor>();
            for (var mongoExecutor : mongoExecutors) {
                executors.add(mapper.mapToDomainEntity(mongoExecutor));
            }
            LOGGER.info("Loaded all Executors from MongoDB");
            return executors;
        } catch (Exception e) {
            LOGGER.warn("Failed to load all Executors from MongoDB");
        }
        return new ArrayList<>();
    }
}
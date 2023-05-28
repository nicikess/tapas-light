package ch.unisg.tapasexecutorpool.pool.application.service;

import ch.unisg.tapascommon.pool.domain.Executor;
import ch.unisg.tapasexecutorpool.pool.application.port.in.RetrieveExecutorFromPoolQuery;
import ch.unisg.tapasexecutorpool.pool.application.port.in.RetrieveExecutorFromPoolUseCase;
import ch.unisg.tapasexecutorpool.pool.application.port.out.LoadExecutorFromRepositoryPort;
import ch.unisg.tapasexecutorpool.pool.domain.ExecutorPool;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Component
@Transactional
public class RetrieveExecutorFromPoolService implements RetrieveExecutorFromPoolUseCase {

    private static final Logger LOGGER = LogManager.getLogger(RetrieveExecutorFromPoolService.class);

    private final LoadExecutorFromRepositoryPort loadExecutorFromRepositoryPort;

    @Override
    public Optional<Executor> retrieveExecutorFromPool(RetrieveExecutorFromPoolQuery query) {
        var executorPool = ExecutorPool.getTapasExecutorPool();
        var executor = executorPool.retrieveExecutorById(query.getExecutorId());

        var executorFromRepo = Optional.ofNullable(
                loadExecutorFromRepositoryPort.loadExecutor(
                        query.getExecutorId(),
                        executorPool.getExecutorPoolName()
                )
        );

        if (executorFromRepo.isPresent()) {
            LOGGER.info("Retrieved Executor from Repository");
            return executorFromRepo;
        }

        LOGGER.info("Retrieved Executor from Cache");
        return executor;
    }
}

package ch.unisg.tapasexecutorpool.pool.application.service;

import ch.unisg.tapasexecutorpool.pool.application.port.in.RetrieveAvailableExecutorsQuery;
import ch.unisg.tapasexecutorpool.pool.application.port.in.RetrieveAvailableExecutorsUseCase;
import ch.unisg.tapascommon.pool.domain.Executor;
import ch.unisg.tapasexecutorpool.pool.domain.ExecutorPool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Component
@Transactional
public class RetrieveAvailableExecutorsService implements RetrieveAvailableExecutorsUseCase {
    @Override
    public List<Executor> retrieveAvailableExecutorsFromPool(RetrieveAvailableExecutorsQuery query) {
        var executorPool = ExecutorPool.getTapasExecutorPool();
        return executorPool.retrieveAvailableExecutors();
    }
}

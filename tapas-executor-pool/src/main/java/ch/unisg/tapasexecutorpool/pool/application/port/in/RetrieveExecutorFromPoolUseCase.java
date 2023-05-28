package ch.unisg.tapasexecutorpool.pool.application.port.in;

import ch.unisg.tapascommon.pool.domain.Executor;
import java.util.Optional;

public interface RetrieveExecutorFromPoolUseCase {
    Optional<Executor> retrieveExecutorFromPool(RetrieveExecutorFromPoolQuery query);
}

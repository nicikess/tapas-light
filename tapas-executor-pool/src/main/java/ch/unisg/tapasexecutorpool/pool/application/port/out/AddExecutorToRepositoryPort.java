package ch.unisg.tapasexecutorpool.pool.application.port.out;

import ch.unisg.tapascommon.pool.domain.Executor;

public interface AddExecutorToRepositoryPort {
    void addExecutor(Executor executor);
}

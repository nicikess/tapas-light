package ch.unisg.tapasexecutorpool.pool.application.port.out;

import ch.unisg.tapasexecutorpool.pool.domain.ExecutorPool;

public interface ExecutorPoolLock {
    void lockExecutorPool(ExecutorPool.ExecutorPoolName executorPoolName);
    void releaseExecutorPool(ExecutorPool.ExecutorPoolName executorPoolName);
}

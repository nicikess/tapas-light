package ch.unisg.tapasexecutorpool.pool.application.service;

import ch.unisg.tapasexecutorpool.pool.application.port.out.ExecutorPoolLock;
import ch.unisg.tapasexecutorpool.pool.domain.ExecutorPool;
import org.springframework.stereotype.Component;

@Component
public class NoOpExecutorPoolLock implements ExecutorPoolLock {
    @Override
    public void lockExecutorPool(ExecutorPool.ExecutorPoolName executorPoolName) {
        // do nothing
    }

    @Override
    public void releaseExecutorPool(ExecutorPool.ExecutorPoolName executorPoolName) {
        // do nothing
    }
}

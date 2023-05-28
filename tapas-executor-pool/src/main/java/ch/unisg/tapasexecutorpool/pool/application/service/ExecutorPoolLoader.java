package ch.unisg.tapasexecutorpool.pool.application.service;

import ch.unisg.tapasexecutorpool.pool.application.port.out.LoadExecutorFromRepositoryPort;
import ch.unisg.tapasexecutorpool.pool.domain.ExecutorPool;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Profile("!test")
@RequiredArgsConstructor
@Component
public class ExecutorPoolLoader {

    private static final Logger LOGGER = LogManager.getLogger(ExecutorPoolLoader.class);

    private final LoadExecutorFromRepositoryPort loadExecutorFromRepositoryPort;

    @EventListener(ApplicationReadyEvent.class)
    public void loadExecutorsFromRepository() {
        var executors = loadExecutorFromRepositoryPort.loadAllExecutors();
        var executorPool = ExecutorPool.getTapasExecutorPool();
        executorPool.clearAllExecutors();
        executorPool.addAllExecutors(executors);
        LOGGER.info("Loaded " + executors.size() + " Executors from Repository");
    }
}

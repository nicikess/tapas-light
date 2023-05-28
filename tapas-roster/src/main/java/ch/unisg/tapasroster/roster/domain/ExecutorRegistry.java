package ch.unisg.tapasroster.roster.domain;

import ch.unisg.tapascommon.pool.domain.Executor;
import ch.unisg.tapascommon.tasks.domain.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class ExecutorRegistry {
    private static final Logger LOGGER = LogManager.getLogger(ExecutorRegistry.class);

    private static ExecutorRegistry registry;

    private final HashSet<Executor> executors;

    private ExecutorRegistry() {
        this.executors = new HashSet<>();
    }

    public static synchronized ExecutorRegistry getInstance() {
        if (registry == null) {
            registry = new ExecutorRegistry();
        }

        return registry;
    }

    public boolean addExecutor(Executor executor) {
        LOGGER.debug("Add Executor to Registry: " + executor);
        var result = executors.add(executor);
        LOGGER.info("Number of available executors: " + executors.size());
        return result;
    }

    public boolean removeExecutor(Executor executor) {
        LOGGER.debug("Remove Executor from Registry: " + executor);
        var result = executors.remove(executor);
        LOGGER.info("Number of available executors: " + executors.size());
        return result;
    }

    public void clearExecutors() {
        LOGGER.debug("Cleared all Executors from Registry");
        executors.clear();
        LOGGER.info("Number of available executors: " + executors.size());
    }

    public boolean hasExecutorWithTaskType(Task.Type type) {
        for (var executor : executors) {
            if (executor.getExecutorType().getValue().equals(type)) {
                return true;
            }
        }
        return false;
    }
}

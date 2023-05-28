package ch.unisg.tapasexecutorpool.pool.domain;

import ch.unisg.tapascommon.pool.domain.Executor;
import ch.unisg.tapascommon.tasks.domain.Task;
import lombok.Getter;
import lombok.Value;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.*;

public class ExecutorPool {

    private static final Logger LOGGER = LogManager.getLogger(ExecutorPool.class);

    private static final ExecutorPool EXECUTOR_POOL = new ExecutorPool(new ExecutorPoolName("tapas-executorpool"));

    @Getter
    private final ExecutorPoolName executorPoolName;

    @Getter
    private final ListOfExecutors listOfExecutors;

    private ExecutorPool(ExecutorPoolName executorPoolName) {
        this.executorPoolName = executorPoolName;
        this.listOfExecutors = new ListOfExecutors(new LinkedList<>());
    }

    public static ExecutorPool getTapasExecutorPool() {
        return EXECUTOR_POOL;
    }

    public boolean addAllExecutors(List<Executor> executors) {
        return listOfExecutors.value.addAll(executors);
    }

    public Executor addNewExecutor(
            Executor.ExecutorId id,
            Executor.ExecutorName name,
            Executor.ExecutorType type,
            Executor.ExecutorAddress address
    ) {
        var newExecutor = new Executor(
                id,
                name,
                type,
                address,
                new Executor.ExecutorState(Executor.State.IDLE),
                new Executor.ExecutorPoolName(executorPoolName.getValue())
        );

        listOfExecutors.value.add(newExecutor);
        LOGGER.info("Added new Executor: " + newExecutor);
        LOGGER.info("Number of Executors: "+ listOfExecutors.value.size());
        return newExecutor;
    }

    public Optional<Executor> retrieveExecutorById(Executor.ExecutorId id) {
        for (var executor : listOfExecutors.value) {
            if (executor.getExecutorId().getValue().equalsIgnoreCase(id.getValue())) {
                return Optional.of(executor);
            }
        }
        return Optional.empty();
    }

    public Optional<Executor> retrieveAvailableExecutorByTaskType(Task.TaskType type) {
        for (var executor : listOfExecutors.value) {
            if (executor.getExecutorType().getValue().name().equalsIgnoreCase(type.getValue())) {
                if (Objects.equals(executor.getExecutorState(), new Executor.ExecutorState(Executor.State.IDLE))) {
                    return Optional.of(executor);
                }
            }
        }
        return Optional.empty();
    }

    public List<Executor> retrieveAvailableExecutors() {
        var availableExecutors = new ArrayList<Executor>();
        for (var executor : listOfExecutors.value) {
            if (executor.getExecutorState().getValue().equals(Executor.State.IDLE)) {
                availableExecutors.add(executor);
            }
        }
        return availableExecutors;
    }

    public boolean updateExecutorState(Executor.ExecutorId executorId, Executor.ExecutorState executorState) {
        var executor = retrieveExecutorById(executorId);
        if (executor.isPresent()) {
            executor.get().setExecutorState(executorState);
            return true;
        }
        return false;
    }

    public void clearAllExecutors() {
        listOfExecutors.value.clear();
    }

    @Value
    public static class ExecutorPoolName {
        String value;
    }

    @Value
    public static class ListOfExecutors {
        List<Executor> value;
    }
}

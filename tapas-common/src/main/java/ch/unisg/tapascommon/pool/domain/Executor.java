package ch.unisg.tapascommon.pool.domain;

import ch.unisg.tapascommon.tasks.domain.Task;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

public class Executor {
    public enum State {
        IDLE, BUSY, RESERVED
    }

    @Getter
    private final ExecutorId executorId;

    @Getter
    private final ExecutorName executorName;

    @Getter
    private final ExecutorType executorType;

    @Getter
    private final ExecutorAddress executorAddress;

    @Getter
    private final ExecutorPoolName executorPoolName;

    @Setter
    @Getter
    private ExecutorState executorState;

    public Executor(
            ExecutorId executorId,
            ExecutorName executorName,
            ExecutorType executorType,
            ExecutorAddress executorAddress,
            ExecutorState executorState,
            ExecutorPoolName executorPoolName) {
        this.executorId = executorId;
        this.executorName = executorName;
        this.executorType = executorType;
        this.executorAddress = executorAddress;
        this.executorState = executorState;
        this.executorPoolName = executorPoolName;
    }

    @Value
    public static class ExecutorId {
        String value;
    }

    @Value
    public static class ExecutorName {
        String value;
    }

    @Value
    public static class ExecutorState {
        State value;
    }

    @Value
    public static class ExecutorType {
        Task.Type value;
    }

    @Value
    public static class ExecutorAddress {
        String value;
    }

    @Value
    public static class ExecutorPoolName {
        String value;
    }

    @Override
    public String toString() {
        return "Executor{" +
                "executorId=" + executorId.getValue() +
                ", executorName=" + executorName.getValue() +
                ", executorType=" + executorType.getValue().name() +
                ", executorAddress=" + executorAddress.getValue() +
                ", executorPoolName=" + executorPoolName.getValue() +
                ", executorState=" + executorState.getValue().name() +
                '}';
    }
}

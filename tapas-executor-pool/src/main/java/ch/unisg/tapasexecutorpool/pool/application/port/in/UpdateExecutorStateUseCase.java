package ch.unisg.tapasexecutorpool.pool.application.port.in;

public interface UpdateExecutorStateUseCase {
    boolean updateExecutorState(UpdateExecutorStateCommand command);
}

package ch.unisg.tapasexecutorbase.executor.application.port.in;

public interface RegisterExecutorToPoolUseCase {
    boolean registerThisExecutorToPool(RegisterExecutorToPoolCommand command);
}

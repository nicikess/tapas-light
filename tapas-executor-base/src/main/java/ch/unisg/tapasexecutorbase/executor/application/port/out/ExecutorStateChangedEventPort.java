package ch.unisg.tapasexecutorbase.executor.application.port.out;

import ch.unisg.tapasexecutorbase.executor.domain.ExecutorStateChangedEvent;

public interface ExecutorStateChangedEventPort {
    void publishExecutorStateChangedEvent(ExecutorStateChangedEvent event);
}

package ch.unisg.tapasexecutorpool.pool.application.port.out;

public interface ForwardTaskToExecutorEventPort {
    void forwardTaskToExecutorEvent(ForwardTaskToExecutorEvent event);
}

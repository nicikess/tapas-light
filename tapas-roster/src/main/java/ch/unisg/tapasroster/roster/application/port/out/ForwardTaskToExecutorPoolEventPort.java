package ch.unisg.tapasroster.roster.application.port.out;

public interface ForwardTaskToExecutorPoolEventPort {
    void forwardTaskToExecutorPoolEvent(ForwardTaskToExecutorPoolEvent event);
}

package ch.unisg.tapasexecutorpool.pool.application.port.out;

public interface TaskAssignedEventPort {
    void handleTaskAssignedEvent(TaskAssignedEvent event);
}
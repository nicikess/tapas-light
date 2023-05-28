package ch.unisg.tapasexecutorbase.executor.application.port.out;

import ch.unisg.tapasexecutorbase.executor.domain.TaskUpdatedEvent;

public interface TaskUpdatedEventPort {
    void updateTaskStatusEvent(TaskUpdatedEvent event);
}
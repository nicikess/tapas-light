package ch.unisg.tapastasks.tasks.application.port.in;

import ch.unisg.tapascommon.tasks.domain.Task;

public interface TaskStartedEventHandler {
    Task handleTaskStarted(TaskStartedEvent taskStartedEvent);
}

package ch.unisg.tapastasks.tasks.application.port.out;

import ch.unisg.tapastasks.tasks.domain.NewTaskAddedEvent;

public interface NewTaskAddedEventPort {
    void publishNewTaskAddedEvent(NewTaskAddedEvent event);
}

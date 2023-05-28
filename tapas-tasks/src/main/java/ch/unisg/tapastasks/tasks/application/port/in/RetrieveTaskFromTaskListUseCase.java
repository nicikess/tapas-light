package ch.unisg.tapastasks.tasks.application.port.in;

import ch.unisg.tapascommon.tasks.domain.Task;

import java.util.Optional;

public interface RetrieveTaskFromTaskListUseCase {
    Optional<Task> retrieveTaskFromTaskList(RetrieveTaskFromTaskListQuery query);
}

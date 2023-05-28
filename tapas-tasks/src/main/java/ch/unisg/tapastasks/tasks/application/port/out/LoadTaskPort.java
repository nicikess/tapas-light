package ch.unisg.tapastasks.tasks.application.port.out;

import ch.unisg.tapascommon.tasks.domain.Task;
import ch.unisg.tapastasks.tasks.domain.TaskList;

public interface LoadTaskPort {
    Task loadTask(Task.TaskId taskId, TaskList.TaskListName taskListName);
}

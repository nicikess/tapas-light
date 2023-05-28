package ch.unisg.tapastasks.tasks.application.handler;

import ch.unisg.tapastasks.tasks.application.port.in.TaskAssignedEvent;
import ch.unisg.tapastasks.tasks.application.port.in.TaskAssignedEventHandler;
import ch.unisg.tapascommon.tasks.domain.Task;
import ch.unisg.tapastasks.tasks.application.port.out.AddTaskPort;
import ch.unisg.tapastasks.tasks.application.port.out.TaskListLock;
import ch.unisg.tapastasks.tasks.domain.TaskList;
import ch.unisg.tapastasks.tasks.domain.TaskNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TaskAssignedHandler implements TaskAssignedEventHandler {

    private static final Logger LOGGER = LogManager.getLogger(TaskAssignedHandler.class);

    @Autowired
    private final AddTaskPort addTaskToRepositoryPort;

    @Autowired
    private final TaskListLock taskListLock;

    @Override
    public Task handleTaskAssigned(TaskAssignedEvent taskAssignedEvent) throws TaskNotFoundException {
        TaskList taskList = TaskList.getTapasTaskList();
        taskListLock.lockTaskList(taskList.getTaskListName());

        var task = taskList.changeTaskStatusToAssigned(taskAssignedEvent.getTaskId(),
            taskAssignedEvent.getServiceProvider());

        addTaskToRepositoryPort.addTask(task);
        taskListLock.releaseTaskList(taskList.getTaskListName());

        LOGGER.info("Handled Task Assigned Event");

        return task;
    }
}

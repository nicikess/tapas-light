package ch.unisg.tapastasks.tasks.application.handler;

import ch.unisg.tapastasks.tasks.application.port.in.TaskStartedEvent;
import ch.unisg.tapastasks.tasks.application.port.in.TaskStartedEventHandler;
import ch.unisg.tapascommon.tasks.domain.Task;
import ch.unisg.tapastasks.tasks.application.port.out.AddTaskPort;
import ch.unisg.tapastasks.tasks.application.port.out.TaskListLock;
import ch.unisg.tapastasks.tasks.domain.TaskList;
import ch.unisg.tapastasks.tasks.domain.TaskNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskStartedHandler implements TaskStartedEventHandler {

    private static final Logger LOGGER = LogManager.getLogger(TaskStartedHandler.class);

    @Autowired
    private AddTaskPort addTaskToRepositoryPort;

    @Autowired
    private TaskListLock taskListLock;

    @Override
    public Task handleTaskStarted(TaskStartedEvent taskStartedEvent) throws TaskNotFoundException {
        TaskList taskList = TaskList.getTapasTaskList();
        taskListLock.lockTaskList(taskList.getTaskListName());

        var task =  taskList.changeTaskStatusToRunning(taskStartedEvent.getTaskId(),
            taskStartedEvent.getServiceProvider());

        addTaskToRepositoryPort.addTask(task);
        taskListLock.releaseTaskList(taskList.getTaskListName());

        LOGGER.info("Handled Task Started Event");

        return task;
    }
}

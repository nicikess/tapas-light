package ch.unisg.tapastasks.tasks.application.service;

import ch.unisg.tapascommon.tasks.domain.Task;
import ch.unisg.tapastasks.tasks.application.port.in.AddNewTaskToTaskListCommand;
import ch.unisg.tapastasks.tasks.application.port.in.AddNewTaskToTaskListUseCase;
import ch.unisg.tapastasks.tasks.application.port.out.AddTaskPort;
import ch.unisg.tapastasks.tasks.application.port.out.NewTaskAddedEventPort;
import ch.unisg.tapastasks.tasks.application.port.out.TaskListLock;
import ch.unisg.tapastasks.tasks.domain.NewTaskAddedEvent;
import ch.unisg.tapastasks.tasks.domain.TaskList;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Component
@Transactional
public class AddNewTaskToTaskListService implements AddNewTaskToTaskListUseCase {

    private static final Logger LOGGER = LogManager.getLogger(AddNewTaskToTaskListService.class);

    private final NewTaskAddedEventPort newTaskAddedEventPort;
    private final AddTaskPort addTaskToRepositoryPort;
    private final TaskListLock taskListLock;

    @Override
    public Task addNewTaskToTaskList(AddNewTaskToTaskListCommand command) {
        var taskList = TaskList.getTapasTaskList();

        taskListLock.lockTaskList(taskList.getTaskListName());

        var newTask = (command.getOriginalTaskUri().isPresent()) ?
            // Create a delegated task that points back to the original task
            taskList.addNewTaskWithNameAndTypeAndOriginalTaskUri(command.getTaskName(),
                command.getTaskType(), command.getOriginalTaskUri().get())
            // Create an original task
            : taskList.addNewTaskWithNameAndType(command.getTaskName(), command.getTaskType());

        // When creating a task, the task's representation may include optional input data
        if (command.getInputData().isPresent()){
            newTask.setInputData(command.getInputData().get());
        }

        addTaskToRepositoryPort.addTask(newTask);
        taskListLock.releaseTaskList(taskList.getTaskListName());

        if (newTask != null) {
            var newTaskAdded = new NewTaskAddedEvent(
                newTask.getTaskName().getValue(),
                taskList.getTaskListName().getValue(),
                newTask.getTaskId().getValue()
            );
            LOGGER.info("Forwarding Task to Roster Service: " + newTask);
            newTaskAddedEventPort.publishNewTaskAddedEvent(newTaskAdded);
        }

        return newTask;
    }
}

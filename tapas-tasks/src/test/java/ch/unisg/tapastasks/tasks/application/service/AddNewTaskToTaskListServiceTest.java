package ch.unisg.tapastasks.tasks.application.service;

import ch.unisg.tapascommon.tasks.domain.Task;
import ch.unisg.tapastasks.tasks.application.port.in.AddNewTaskToTaskListCommand;
import ch.unisg.tapastasks.tasks.application.port.out.AddTaskPort;
import ch.unisg.tapastasks.tasks.application.port.out.NewTaskAddedEventPort;
import ch.unisg.tapastasks.tasks.application.port.out.TaskListLock;
import ch.unisg.tapastasks.tasks.domain.NewTaskAddedEvent;
import ch.unisg.tapastasks.tasks.domain.TaskList;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

public class AddNewTaskToTaskListServiceTest {

    private final AddTaskPort addTaskPort = Mockito.mock(AddTaskPort.class);
    private final TaskListLock taskListLock = Mockito.mock(TaskListLock.class);
    private final NewTaskAddedEventPort newTaskAddedEventPort = Mockito.mock(NewTaskAddedEventPort.class);
    private final AddNewTaskToTaskListService addNewTaskToTaskListService = new AddNewTaskToTaskListService(
        newTaskAddedEventPort, addTaskPort, taskListLock);

    @Test
    void addingSucceeds() {
        var newTask = givenATaskWithNameAndTypeAndURI(
            new Task.TaskName("test-task"),
            new Task.TaskType("test-type"),
            Optional.of(new Task.InputData("test-input-data")),
            Optional.of(new Task.OriginalTaskUri("example.org"))
        );

        var taskList = givenAnEmptyTaskList(TaskList.getTapasTaskList());

        var addNewTaskToTaskListCommand = new AddNewTaskToTaskListCommand(
            newTask.getTaskName(),
            newTask.getTaskType(),
            Optional.ofNullable(newTask.getInputData()),
            Optional.ofNullable(newTask.getOriginalTaskUri())
        );

        var addedTask = addNewTaskToTaskListService.addNewTaskToTaskList(addNewTaskToTaskListCommand);

        assertThat(addedTask).isNotNull();
        assertThat(taskList.getListOfTasks().getValue()).hasSize(1);

        then(taskListLock).should().lockTaskList(eq(TaskList.getTapasTaskList().getTaskListName()));
        then(newTaskAddedEventPort).should(times(1))
            .publishNewTaskAddedEvent(any(NewTaskAddedEvent.class));
    }

    private TaskList givenAnEmptyTaskList(TaskList taskList) {
        taskList.getListOfTasks().getValue().clear();
        return taskList;
    }

    private Task givenATaskWithNameAndTypeAndURI(
        Task.TaskName taskName,
        Task.TaskType taskType,
        Optional<Task.InputData> inputData,
        Optional<Task.OriginalTaskUri> originalTaskUri
    ) {
        var task = Mockito.mock(Task.class);
        given(task.getTaskName()).willReturn(taskName);
        given(task.getTaskType()).willReturn(taskType);
        given(task.getInputData()).willReturn(inputData.get());
        given(task.getOriginalTaskUri()).willReturn(originalTaskUri.get());
        return task;
    }
}

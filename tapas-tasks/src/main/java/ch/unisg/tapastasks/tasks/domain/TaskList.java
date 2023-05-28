package ch.unisg.tapastasks.tasks.domain;

import ch.unisg.tapascommon.tasks.domain.Task;
import lombok.Getter;
import lombok.Value;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class TaskList {

    private static final Logger LOGGER = LogManager.getLogger(TaskList.class);

    @Getter
    private final TaskListName taskListName;

    @Getter
    private final ListOfTasks listOfTasks;

    private static final TaskList taskList = new TaskList(new TaskListName("tapas-tasks-group4"));

    private TaskList(TaskListName taskListName) {
        this.taskListName = taskListName;
        this.listOfTasks = new ListOfTasks(new LinkedList<Task>());
    }

    public static TaskList getTapasTaskList() {
        return taskList;
    }

    public Task addNewTaskWithNameAndType(Task.TaskName name, Task.TaskType type) {
        var newTask = Task.createTaskWithNameAndType(name, type);
        this.addNewTaskToList(newTask);
        return newTask;
    }

    public Task addNewTaskWithNameAndTypeAndOriginalTaskUri(
        Task.TaskName name,
        Task.TaskType type,
        Task.OriginalTaskUri originalTaskUri
    ) {
        var newTask = Task.createTaskWithNameAndTypeAndOriginalTaskUri(name, type, originalTaskUri);
        this.addNewTaskToList(newTask);
        return newTask;
    }

    private void addNewTaskToList(Task newTask) {
        listOfTasks.value.add(newTask);
        LOGGER.info("Number of Tasks: " + listOfTasks.value.size());
    }

    public Optional<Task> retrieveTaskById(Task.TaskId id) {
        for (var task : listOfTasks.value) {
            if (task.getTaskId().getValue().equalsIgnoreCase(id.getValue())) {
                return Optional.of(task);
            }
        }

        return Optional.empty();
    }

    public Task changeTaskStatusToAssigned(Task.TaskId id, Optional<Task.ServiceProvider> serviceProvider)
            throws TaskNotFoundException {
        return changeTaskStatus(id, new Task.TaskStatus(Task.Status.ASSIGNED), serviceProvider, Optional.empty());
    }

    public Task changeTaskStatusToRunning(Task.TaskId id, Optional<Task.ServiceProvider> serviceProvider)
            throws TaskNotFoundException {
        return changeTaskStatus(id, new Task.TaskStatus(Task.Status.RUNNING), serviceProvider, Optional.empty());
    }

    public Task changeTaskStatusToExecuted(Task.TaskId id, Optional<Task.ServiceProvider> serviceProvider,
            Optional<Task.OutputData> outputData) throws TaskNotFoundException {
        return changeTaskStatus(id, new Task.TaskStatus(Task.Status.EXECUTED), serviceProvider, outputData);
    }

    private Task changeTaskStatus(Task.TaskId id, Task.TaskStatus status, Optional<Task.ServiceProvider> serviceProvider,
            Optional<Task.OutputData> outputData) {
        var taskOpt = retrieveTaskById(id);

        if (taskOpt.isEmpty()) {
            throw new TaskNotFoundException();
        }

        var task = taskOpt.get();

        if (task.getTaskStatus().getValue().ordinal() > status.getValue().ordinal()) {
            return task;
        }

        task.setTaskStatus(status);

        if (serviceProvider.isPresent()) {
            task.setProvider(serviceProvider.get());
        }

        if (outputData.isPresent()) {
            task.setOutputData(outputData.get());
        }

        return task;
    }

    @Value
    public static class TaskListName {
        String value;
    }

    @Value
    public static class ListOfTasks {
        List<Task> value;
    }
}

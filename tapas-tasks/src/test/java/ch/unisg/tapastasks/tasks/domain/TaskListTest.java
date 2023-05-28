package ch.unisg.tapastasks.tasks.domain;

import ch.unisg.tapascommon.tasks.domain.Task;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class TaskListTest {

    @Test
    void addNewTaskToTaskListSuccess() {
        var taskList = TaskList.getTapasTaskList();
        taskList.getListOfTasks().getValue().clear();

        var newTask = taskList.addNewTaskWithNameAndType(
            new Task.TaskName("My-Test-Task"),
            new Task.TaskType("My-Test-Type")
        );

        assertThat(newTask.getTaskName().getValue()).isEqualTo("My-Test-Task");
        assertThat(taskList.getListOfTasks().getValue()).hasSize(1);
        assertThat(taskList.getListOfTasks().getValue().get(0)).isEqualTo(newTask);
    }

    @Test
    void retrieveTaskSuccess() {
        var taskList = TaskList.getTapasTaskList();

        var newTask = taskList.addNewTaskWithNameAndType(
            new Task.TaskName("My-Test-Task2"),
            new Task.TaskType("My-Test-Type2")
        );

        var retrievedTask = taskList.retrieveTaskById(newTask.getTaskId()).get();

        assertThat(retrievedTask).isEqualTo(newTask);
    }

    @Test
    void retrieveTaskFailure() {
        var taskList = TaskList.getTapasTaskList();

        var newTask = taskList.addNewTaskWithNameAndType(
            new Task.TaskName("My-Test-Task3"),
            new Task.TaskType("My-Test-Type3")
        );

        var fakeId = new Task.TaskId("fake-id");

        var retrievedTask = taskList.retrieveTaskById(fakeId);

        assertThat(retrievedTask.isPresent()).isFalse();
    }
}

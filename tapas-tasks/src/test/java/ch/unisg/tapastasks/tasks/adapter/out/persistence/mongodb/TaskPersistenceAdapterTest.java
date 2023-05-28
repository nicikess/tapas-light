package ch.unisg.tapastasks.tasks.adapter.out.persistence.mongodb;

import ch.unisg.tapascommon.tasks.domain.Task;
import ch.unisg.tapastasks.tasks.domain.TaskList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureDataMongo
@Import({TaskPersistenceAdapter.class, TaskMapper.class})
public class TaskPersistenceAdapterTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskPersistenceAdapter adapterUnderTest;

    @Test
    void addsNewTask() {

        var testTaskId = UUID.randomUUID().toString();
        var testTaskName = "adds-persistence-task-name";
        var testTaskType = "adds-persistence-task-type";
        var testTaskUri = "adds-persistence-test-task-uri";
        var testTaskStatus = Task.Status.OPEN.toString();
        var testTaskListName = "tapas-tasks-group4";
        var testServiceProvider = "tapas-tasks-group4";
        var testInputData = "test-input-data";
        var testOutputData= "test-outputData";

        var testTask = new Task(
            new Task.TaskId(testTaskId),
            new Task.TaskName(testTaskName),
            new Task.TaskType(testTaskType),
            new Task.OriginalTaskUri(testTaskUri),
            new Task.TaskStatus(Task.Status.valueOf(testTaskStatus)),
            new Task.ServiceProvider(testServiceProvider),
            new Task.InputData(testInputData),
            new Task.OutputData(testOutputData)
        );
        adapterUnderTest.addTask(testTask);

        var retrievedDoc = taskRepository.findByTaskId(testTaskId, testTaskListName);

        assertThat(retrievedDoc.getTaskId()).isEqualTo(testTaskId);
        assertThat(retrievedDoc.getTaskName()).isEqualTo(testTaskName);
        assertThat(retrievedDoc.getTaskListName()).isEqualTo(testTaskListName);
    }

    @Test
    void retrievesTask() {

        var testTaskId = UUID.randomUUID().toString();
        var testTaskName = "reads-persistence-task-name";
        var testTaskType = "reads-persistence-task-type";
        var testTaskUri = "reads-persistence-test-task-uri";
        var testTaskStatus = Task.Status.OPEN.toString();
        var testTaskListName = "tapas-tasks-group4";
        var testServiceProvider = "tapas-tasks-group4";
        var testInputData = "test-input-data";
        var testOutputData= "test-outputData";

        var mongoTask = new MongoTaskDocument(
            testTaskId,
            testTaskName,
            testTaskType,
            testTaskUri,
            testTaskStatus,
            testServiceProvider,
            testInputData,
            testOutputData,
            testTaskListName
        );
        taskRepository.insert(mongoTask);

        Task retrievedTask = adapterUnderTest.loadTask(
            new Task.TaskId(testTaskId),
            new TaskList.TaskListName(testTaskListName)
        );

        assertThat(retrievedTask.getTaskName().getValue()).isEqualTo(testTaskName);
        assertThat(retrievedTask.getTaskId().getValue()).isEqualTo(testTaskId);
        assertThat(retrievedTask.getTaskStatus().getValue()).isEqualTo(Task.Status.valueOf(testTaskStatus));
    }
}

package ch.unisg.tapasexecutorpool;

import ch.unisg.tapascommon.tasks.adapter.common.formats.TaskJsonRepresentation;
import ch.unisg.tapascommon.tasks.domain.Task;
import ch.unisg.tapasexecutorpool.pool.domain.ExecutorPool;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExecuteTaskSystemTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void executeTask() throws JsonProcessingException {

        var taskName = new Task.TaskName("test-task-computation");
        var taskType = new Task.TaskType(Task.Type.COMPUTATION.name());
        var taskToSchedule = Task.createTaskWithNameAndType(taskName, taskType);

        var response = whenExecuteTask(taskToSchedule);
        var taskAfterAssignment = response.getBody();

        then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        then(taskAfterAssignment.getTaskName()).isEqualTo(taskName.getValue());
        then(taskAfterAssignment.getTaskType()).isEqualTo(taskType.getValue());

        var pool = ExecutorPool.getTapasExecutorPool();
        var executorOptional = pool.retrieveAvailableExecutorByTaskType(taskToSchedule.getTaskType());

        if (executorOptional.isPresent()) {
            then(taskAfterAssignment.getTaskStatus()).isEqualTo(Task.Status.ASSIGNED.name());
        } else {
            then(taskAfterAssignment.getTaskStatus()).isEqualTo(Task.Status.OPEN.name());
        }
    }

    private ResponseEntity<TaskJsonRepresentation> whenExecuteTask(Task taskToSchedule) throws JsonProcessingException {

        var headers = new HttpHeaders();
        headers.add("Content-Type", TaskJsonRepresentation.MEDIA_TYPE);
        var taskToScheduleJson = TaskJsonRepresentation.serialize(taskToSchedule);
        var request = new HttpEntity<>(taskToScheduleJson, headers);
        return restTemplate.exchange("/execute-task/", HttpMethod.POST, request, TaskJsonRepresentation.class);
    }
}

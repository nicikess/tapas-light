package ch.unisg.tapastasks.tasks.adapter.in.web;

import ch.unisg.tapascommon.tasks.adapter.common.formats.TaskJsonRepresentation;
import ch.unisg.tapascommon.tasks.domain.Task;
import ch.unisg.tapastasks.tasks.adapter.out.persistence.mongodb.TaskRepository;
import ch.unisg.tapastasks.tasks.application.port.in.AddNewTaskToTaskListCommand;
import ch.unisg.tapastasks.tasks.application.port.in.AddNewTaskToTaskListUseCase;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;

import static org.mockito.BDDMockito.eq;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AddNewTaskToTaskListWebController.class)
public class AddNewTaskToTaskListWebControllerTest {

    @MockBean
    private AddNewTaskToTaskListUseCase addNewTaskToTaskListUseCase;

    @MockBean
    private TaskRepository taskRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testAddNewTaskToTaskList() throws Exception {

        var taskName = "test-request";
        var taskType = "test-request-type";
        var inputData = "test-input-data";
        var originalTaskUri = "example.org";

        var jsonPayLoad = new JSONObject()
            .put("taskName", taskName)
            .put("taskType", taskType)
            .put("originalTaskUri", originalTaskUri)
            .put("inputData", inputData)
            .toString();

        var taskStub = Task.createTaskWithNameAndTypeAndOriginalTaskUri(
            new Task.TaskName(taskName),
            new Task.TaskType(taskType),
            new Task.OriginalTaskUri(originalTaskUri)
        );

        var addNewTaskToTaskListCommand = new AddNewTaskToTaskListCommand(
            new Task.TaskName(taskName),
            new Task.TaskType(taskType),
            Optional.of(new Task.InputData(inputData)),
            Optional.of(new Task.OriginalTaskUri(originalTaskUri))
        );

        Mockito.when(addNewTaskToTaskListUseCase.addNewTaskToTaskList(addNewTaskToTaskListCommand))
            .thenReturn(taskStub);

        mockMvc.perform(post("/tasks/")
                .contentType(TaskJsonRepresentation.MEDIA_TYPE)
                .content(jsonPayLoad))
                .andExpect(status().isCreated());

        then(addNewTaskToTaskListUseCase).should()
            .addNewTaskToTaskList(eq(new AddNewTaskToTaskListCommand(
                new Task.TaskName(taskName),
                new Task.TaskType(taskType),
                Optional.of(new Task.InputData(inputData)),
                Optional.of(new Task.OriginalTaskUri(originalTaskUri))
            )));
    }
}

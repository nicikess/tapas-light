package ch.unisg.tapasexecutorpool.pool.adapter.in.web;

import ch.unisg.tapascommon.pool.adapter.common.formats.ExecutorJsonRepresentation;
import ch.unisg.tapascommon.pool.domain.Executor;
import ch.unisg.tapascommon.tasks.domain.Task;
import ch.unisg.tapasexecutorpool.pool.adapter.out.persistence.mongodb.ExecutorRepository;
import ch.unisg.tapasexecutorpool.pool.adapter.out.persistence.mongodb.TaskAssignmentRepository;
import ch.unisg.tapasexecutorpool.pool.application.port.in.AddNewExecutorToPoolCommand;
import ch.unisg.tapasexecutorpool.pool.application.port.in.AddNewExecutorToPoolUseCase;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.eq;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(controllers = AddNewExecutorToPoolWebController.class)
public class AddNewExecutorToPoolWebControllerTest {

    @MockBean
    private AddNewExecutorToPoolUseCase addNewExecutorToPoolUseCase;

    @MockBean
    private ExecutorRepository executorRepository;

    @MockBean
    private TaskAssignmentRepository taskAssignmentRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testAddNewTaskToTaskList() throws Exception {

        var executorId = "test-executor-id";
        var executorName = "test-executor-name";
        var executorType = Task.Type.UNKNOWN;
        var executorAddress = "http://127.0.0.1:8085";

        var executorStub = new Executor(
                new Executor.ExecutorId(executorId),
                new Executor.ExecutorName(executorName),
                new Executor.ExecutorType(executorType),
                new Executor.ExecutorAddress(executorAddress),
                new Executor.ExecutorState(Executor.State.IDLE),
                new Executor.ExecutorPoolName("test-pool")
        );

        var addNewExecutorToPoolCommand = new AddNewExecutorToPoolCommand(
                executorId, executorName, executorType.name(), executorAddress
        );

        Mockito.when(addNewExecutorToPoolUseCase.addNewExecutorToPool(addNewExecutorToPoolCommand))
            .thenReturn(executorStub);

        var jsonPayLoad = new JSONObject()
                .put("executorId", executorId)
                .put("executorName", executorName)
                .put("executorType", executorType.name())
                .put("executorAddress", executorAddress)
                .toString();

        mockMvc.perform(post("/executors/")
                .contentType(ExecutorJsonRepresentation.MEDIA_TYPE)
                .content(jsonPayLoad))
                .andExpect(status().isCreated());

        then(addNewExecutorToPoolUseCase).should().addNewExecutorToPool(eq(addNewExecutorToPoolCommand));
    }
}

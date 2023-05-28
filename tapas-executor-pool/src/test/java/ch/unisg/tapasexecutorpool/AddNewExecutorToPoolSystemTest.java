package ch.unisg.tapasexecutorpool;

import ch.unisg.tapascommon.pool.adapter.common.formats.ExecutorJsonRepresentation;
import ch.unisg.tapascommon.pool.domain.Executor;
import ch.unisg.tapascommon.tasks.adapter.common.formats.TaskJsonRepresentation;
import ch.unisg.tapascommon.tasks.domain.Task;
import ch.unisg.tapasexecutorpool.pool.application.port.out.AddExecutorToRepositoryPort;
import ch.unisg.tapasexecutorpool.pool.domain.ExecutorPool;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddNewExecutorToPoolSystemTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AddExecutorToRepositoryPort addExecutorToRepositoryPort;

    @Test
    void addNewExecutorToPool() throws JSONException {

        var executorId = new Executor.ExecutorId("test-executor-id");
        var executorName = new Executor.ExecutorName("test-executor-name");
        var executorType = new Executor.ExecutorType(Task.Type.UNKNOWN);
        var executorAddress = new Executor.ExecutorAddress("test-executor-address");

        var response = whenAddNewExecutorToPool(executorId, executorName, executorType, executorAddress);

        var responseJson = new JSONObject(response.getBody().toString());
        var responseExecutorId = responseJson.getString("executorId");
        var responseExecutorName = responseJson.getString("executorName");
        var responseExecutorType = responseJson.getString("executorType");
        var responseExecutorAddress = responseJson.getString("executorAddress");
        var responseExecutorState = responseJson.getString("executorState");
        var responseExecutorPoolName = responseJson.getString("executorPoolName");

        then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        then(responseExecutorId).isEqualTo(executorId.getValue());
        then(responseExecutorName).isEqualTo(executorName.getValue());
        then(responseExecutorType).isEqualTo(executorType.getValue().name());
        then(responseExecutorAddress).isEqualTo(executorAddress.getValue());
        then(responseExecutorState).isEqualTo(Executor.State.IDLE.name());
        then(responseExecutorPoolName).isEqualTo(ExecutorPool.getTapasExecutorPool().getExecutorPoolName().getValue());
        then(ExecutorPool.getTapasExecutorPool().getListOfExecutors().getValue()).hasSize(1);
    }

    private ResponseEntity whenAddNewExecutorToPool(
            Executor.ExecutorId executorId,
            Executor.ExecutorName executorName,
            Executor.ExecutorType executorType,
            Executor.ExecutorAddress executorAddress
    ) throws JSONException {

        ExecutorPool.getTapasExecutorPool().getListOfExecutors().getValue().clear();

        var headers = new HttpHeaders();
        headers.add("Content-Type", ExecutorJsonRepresentation.MEDIA_TYPE);

        var jsonPayLoad = new JSONObject()
                .put("executorId", executorId.getValue() )
                .put("executorName", executorName.getValue())
                .put("executorType", executorType.getValue().name())
                .put("executorAddress", executorAddress.getValue())
                .toString();

        var request = new HttpEntity<>(jsonPayLoad, headers);

        return restTemplate.exchange("/executors/", HttpMethod.POST, request, Object.class);
    }
}

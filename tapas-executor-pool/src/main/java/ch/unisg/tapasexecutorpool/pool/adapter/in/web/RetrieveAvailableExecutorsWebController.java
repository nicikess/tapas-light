package ch.unisg.tapasexecutorpool.pool.adapter.in.web;

import ch.unisg.tapascommon.pool.adapter.common.formats.ExecutorJsonRepresentation;
import ch.unisg.tapasexecutorpool.pool.application.port.in.RetrieveAvailableExecutorsQuery;
import ch.unisg.tapasexecutorpool.pool.application.port.in.RetrieveAvailableExecutorsUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RetrieveAvailableExecutorsWebController {

    private final RetrieveAvailableExecutorsUseCase retrieveAvailableExecutorsUseCase;

    @GetMapping(path = "/available-executors/")
    public ResponseEntity<String> retrieveTaskFromTaskList() {
        var command = new RetrieveAvailableExecutorsQuery();
        var availableExecutors = retrieveAvailableExecutorsUseCase.retrieveAvailableExecutorsFromPool(command);

        var jsonBuffer = new StringBuilder();
        jsonBuffer.append("[\n");
        if (!availableExecutors.isEmpty()) {
            for (var executor : availableExecutors) {
                var executorJson = "";
                try {
                    executorJson = ExecutorJsonRepresentation.serialize(executor);
                } catch (JsonProcessingException e) {
                    continue;
                }
                jsonBuffer.append(executorJson);
                jsonBuffer.append(",\n");
            }
            jsonBuffer.deleteCharAt(jsonBuffer.lastIndexOf(","));
        }
        jsonBuffer.append("]");

        var json = jsonBuffer.toString();
        var responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.CONTENT_TYPE, ExecutorJsonRepresentation.MEDIA_TYPE);
        return new ResponseEntity<>(json, responseHeaders, HttpStatus.OK);
    }
}

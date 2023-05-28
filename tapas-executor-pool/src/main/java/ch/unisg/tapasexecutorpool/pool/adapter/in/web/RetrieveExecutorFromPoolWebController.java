package ch.unisg.tapasexecutorpool.pool.adapter.in.web;

import ch.unisg.tapascommon.pool.adapter.common.formats.ExecutorJsonRepresentation;
import ch.unisg.tapasexecutorpool.pool.application.port.in.RetrieveExecutorFromPoolQuery;
import ch.unisg.tapasexecutorpool.pool.application.port.in.RetrieveExecutorFromPoolUseCase;
import ch.unisg.tapascommon.pool.domain.Executor;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RetrieveExecutorFromPoolWebController {

    private final RetrieveExecutorFromPoolUseCase retrieveExecutorFromPoolUseCase;

    @GetMapping(path = "/executors/{executorId}")
    public ResponseEntity<String> retrieveTaskFromTaskList(@PathVariable("executorId") String executorId) {
        var command = new RetrieveExecutorFromPoolQuery(new Executor.ExecutorId(executorId));
        var executorOptional = retrieveExecutorFromPoolUseCase.retrieveExecutorFromPool(command);

        if (executorOptional.isPresent()) {
            var responseHeaders = new HttpHeaders();
            responseHeaders.add(HttpHeaders.CONTENT_TYPE, ExecutorJsonRepresentation.MEDIA_TYPE);
            try {
                return new ResponseEntity<>(
                        ExecutorJsonRepresentation.serialize(executorOptional.get()),
                        responseHeaders,
                        HttpStatus.OK
                );
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

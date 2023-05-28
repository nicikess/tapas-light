package ch.unisg.tapasexecutorpool.pool.adapter.in.web;

import ch.unisg.tapascommon.pool.adapter.common.formats.ExecutorJsonRepresentation;
import ch.unisg.tapasexecutorpool.pool.application.port.in.AddNewExecutorToPoolCommand;
import ch.unisg.tapasexecutorpool.pool.application.port.in.AddNewExecutorToPoolUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.ConstraintViolationException;

@RequiredArgsConstructor
@RestController
public class AddNewExecutorToPoolWebController {

    private final AddNewExecutorToPoolUseCase addNewExecutorToPoolUseCase;

    @PostMapping(path = "/executors/", consumes = {ExecutorJsonRepresentation.MEDIA_TYPE})
    public ResponseEntity<String> addNewExecutorToPool(@RequestBody ExecutorJsonRepresentation executorJsonRepresentation) {
        try {
            var newExecutor = addNewExecutorToPoolUseCase.addNewExecutorToPool(
                    new AddNewExecutorToPoolCommand(
                            executorJsonRepresentation.getExecutorId(),
                            executorJsonRepresentation.getExecutorName(),
                            executorJsonRepresentation.getExecutorType(),
                            executorJsonRepresentation.getExecutorAddress()
                    )
            );
            var responseHeaders = new HttpHeaders();
            responseHeaders.add(HttpHeaders.CONTENT_TYPE, ExecutorJsonRepresentation.MEDIA_TYPE);
            return new ResponseEntity<>(ExecutorJsonRepresentation.serialize(newExecutor), responseHeaders, HttpStatus.CREATED);
        } catch (ConstraintViolationException | JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}

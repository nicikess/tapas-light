package ch.unisg.tapasexecutorpool.pool.adapter.in.web;

import ch.unisg.tapascommon.tasks.adapter.common.formats.TaskJsonRepresentation;
import ch.unisg.tapasexecutorpool.pool.application.port.in.ExecuteTaskCommand;
import ch.unisg.tapasexecutorpool.pool.application.port.in.ExecuteTaskUseCase;
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
public class ExecuteTaskWebController {

    private final ExecuteTaskUseCase executeTaskUseCase;

    @PostMapping(path = "/execute-task/", consumes = {TaskJsonRepresentation.MEDIA_TYPE})
    public ResponseEntity<String> executeTask(@RequestBody TaskJsonRepresentation task) {
        try {
            var command = new ExecuteTaskCommand(task.deserialize());
            var newTask = executeTaskUseCase.executeTask(command);
            var responseHeaders = new HttpHeaders();
            responseHeaders.add(HttpHeaders.CONTENT_TYPE, TaskJsonRepresentation.MEDIA_TYPE);
            return new ResponseEntity<>(TaskJsonRepresentation.serialize(newTask), responseHeaders, HttpStatus.CREATED);
        } catch (ConstraintViolationException | JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}

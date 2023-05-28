package ch.unisg.tapasexecutorbase.executor.adapter.in.web;

import ch.unisg.tapascommon.tasks.adapter.common.formats.TaskJsonRepresentation;
import ch.unisg.tapasexecutorbase.executor.application.port.in.ExecuteTaskCommand;
import ch.unisg.tapasexecutorbase.executor.application.port.in.ExecuteTaskUseCase;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;

@RequiredArgsConstructor
@RestController
public class ExecuteTaskWebController {

    private static final Logger LOGGER = LogManager.getLogger(ExecuteTaskWebController.class);

    private final ExecuteTaskUseCase executeTaskUseCase;

    @PostMapping(path = "/execute-task", consumes = {TaskJsonRepresentation.MEDIA_TYPE})
    public ResponseEntity<Void> executeTask(@RequestBody TaskJsonRepresentation task) {
        LOGGER.info("Received new Task to execute");
        try {
            var command = new ExecuteTaskCommand(task.deserialize());
            executeTaskUseCase.executeTask(command);
            return ResponseEntity.ok().build();
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}

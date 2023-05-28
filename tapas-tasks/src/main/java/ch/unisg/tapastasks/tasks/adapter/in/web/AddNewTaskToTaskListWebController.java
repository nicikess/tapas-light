package ch.unisg.tapastasks.tasks.adapter.in.web;

import ch.unisg.tapascommon.configuration.ServiceConfiguration;
import ch.unisg.tapascommon.tasks.adapter.common.formats.TaskJsonRepresentation;
import ch.unisg.tapastasks.tasks.application.port.in.AddNewTaskToTaskListCommand;
import ch.unisg.tapastasks.tasks.application.port.in.AddNewTaskToTaskListUseCase;
import ch.unisg.tapascommon.tasks.domain.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

/**
 * Controller that handles HTTP requests for creating new tasks. This controller implements the
 * {@link AddNewTaskToTaskListUseCase} use case using the {@link AddNewTaskToTaskListCommand}.
 *
 * A new task is created via an HTTP POST request to the /tasks/ endpoint. The body of the request
 * contains a JSON-based representation with the "application/task+json" media type defined for this
 * project. This custom media type allows to capture the semantics of our JSON representations for
 * tasks.
 *
 * If the request is successful, the controller returns an HTTP 201 Created status code and a
 * representation of the created task with Content-Type "application/task+json". The HTTP response
 * also include a Location header field that points to the URI of the created task.
 */
@RequiredArgsConstructor
@RestController
public class AddNewTaskToTaskListWebController {

    @Autowired
    private final AddNewTaskToTaskListUseCase addNewTaskToTaskListUseCase;

    @PostMapping(path = { "/tasks", "/tasks/" }, consumes = { TaskJsonRepresentation.MEDIA_TYPE })
    public ResponseEntity<String> addNewTaskTaskToTaskList(@RequestBody TaskJsonRepresentation payload) {
        try {
            var taskName = new Task.TaskName(payload.getTaskName());
            var taskType = new Task.TaskType(payload.getTaskType());

            Optional<Task.InputData> inputDataOptional = (payload.getInputData() == null) ? Optional.empty()
                : Optional.of(new Task.InputData(payload.getInputData()));

            // If the created task is a delegated task, the representation contains a URI reference
            // to the original task
            Optional<Task.OriginalTaskUri> originalTaskUriOptional =
                (payload.getOriginalTaskUri() == null) ? Optional.empty()
                : Optional.of(new Task.OriginalTaskUri(payload.getOriginalTaskUri()));

            AddNewTaskToTaskListCommand command = new AddNewTaskToTaskListCommand(
                taskName,
                taskType,
                inputDataOptional,
                originalTaskUriOptional
            );

            var createdTask = addNewTaskToTaskListUseCase.addNewTaskToTaskList(command);

            // Add the content type as a response header
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add(HttpHeaders.CONTENT_TYPE, TaskJsonRepresentation.MEDIA_TYPE);
            // Construct and advertise the URI of the newly created task
            responseHeaders.add(HttpHeaders.LOCATION, ServiceConfiguration.getTaskListAddress()
                + "tasks/" + createdTask.getTaskId().getValue());

            return new ResponseEntity<>(TaskJsonRepresentation.serialize(createdTask), responseHeaders,
                HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}

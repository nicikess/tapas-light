package ch.unisg.tapastasks.tasks.adapter.in.web;

import ch.unisg.tapascommon.tasks.adapter.common.formats.TaskJsonRepresentation;
import ch.unisg.tapastasks.tasks.application.port.in.RetrieveTaskFromTaskListQuery;
import ch.unisg.tapastasks.tasks.application.port.in.RetrieveTaskFromTaskListUseCase;
import ch.unisg.tapascommon.tasks.domain.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * Controller that handles HTTP GET requests for retrieving tasks. This controller implements the
 * {@link RetrieveTaskFromTaskListUseCase} use case using the {@link RetrieveTaskFromTaskListQuery}
 * query.
 */
@RestController
public class RetrieveTaskFromTaskListWebController {
    private final RetrieveTaskFromTaskListUseCase retrieveTaskFromTaskListUseCase;

    public RetrieveTaskFromTaskListWebController(RetrieveTaskFromTaskListUseCase retrieveTaskFromTaskListUseCase) {
        this.retrieveTaskFromTaskListUseCase = retrieveTaskFromTaskListUseCase;
    }

    /**
     * Retrieves a representation of task. Returns HTTP 200 OK if the request is successful with a
     * representation of the task using the Content-Type "applicatoin/task+json".
     *
     * @param taskId the local identifier of the requested task (extracted from the task's URI)
     * @return a representation of the task if the task exists
     */
    @GetMapping(path = "/tasks/{taskId}")
    public ResponseEntity<String> retrieveTaskFromTaskList(@PathVariable("taskId") String taskId) {
        RetrieveTaskFromTaskListQuery query = new RetrieveTaskFromTaskListQuery(new Task.TaskId(taskId));
        Optional<Task> updatedTaskOpt = retrieveTaskFromTaskListUseCase.retrieveTaskFromTaskList(query);

        // Check if the task with the given identifier exists
        if (updatedTaskOpt.isEmpty()) {
            // If not, through a 404 Not Found status code
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        try {
            String taskRepresentation = TaskJsonRepresentation.serialize(updatedTaskOpt.get());

            // Add the content type as a response header
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add(HttpHeaders.CONTENT_TYPE, TaskJsonRepresentation.MEDIA_TYPE);

            return new ResponseEntity<>(taskRepresentation, responseHeaders, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}

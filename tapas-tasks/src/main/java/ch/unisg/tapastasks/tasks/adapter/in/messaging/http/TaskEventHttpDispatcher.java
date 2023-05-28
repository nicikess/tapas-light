package ch.unisg.tapastasks.tasks.adapter.in.messaging.http;

import ch.unisg.tapascommon.tasks.adapter.common.formats.TaskJsonPatchRepresentation;
import ch.unisg.tapascommon.tasks.adapter.common.formats.TaskJsonRepresentation;
import ch.unisg.tapastasks.tasks.adapter.in.messaging.UnknownEventException;
import ch.unisg.tapascommon.tasks.domain.Task;
import ch.unisg.tapastasks.tasks.domain.TaskNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonpatch.JsonPatch;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.util.Optional;


/**
 * This REST Controller handles HTTP PATCH requests for updating the representational state of Task
 * resources. Each request to update the representational state of a Task resource can correspond to
 * at most one domain/integration event. Request payloads use the
 * <a href="http://jsonpatch.com/">JSON PATCH</a> format and media type.
 *
 * A JSON Patch can contain multiple operations (e.g., add, remove, replace) for updating various
 * parts of a task's representations. One or more JSON Patch operations can represent a domain/integration
 * event. Therefore, the events can only be determined by inspecting the requested patch (e.g., a request
 * to change a task's status from RUNNING to EXECUTED). This class is responsible to inspect requested
 * patches, identify events, and to route them to appropriate listeners.
 *
 * For more details on JSON Patch, see: <a href="http://jsonpatch.com/">http://jsonpatch.com/</a>
 * For some sample HTTP requests, see the README.
 */
@RequiredArgsConstructor
@RestController
public class TaskEventHttpDispatcher {
    // The standard media type for JSON Patch registered with IANA
    // See: https://www.iana.org/assignments/media-types/application/json-patch+json
    private final static String JSON_PATCH_MEDIA_TYPE = "application/json-patch+json";

    private static final Logger LOGGER = LogManager.getLogger(TaskEventHttpDispatcher.class);

    @Autowired
    private final TaskAssignedEventListenerHttpAdapter taskAssignedEventListenerHttpAdapter;

    @Autowired
    private final TaskStartedEventListenerHttpAdapter taskStartedEventListenerHttpAdapter;

    @Autowired
    private final TaskExecutedEventListenerHttpAdapter taskExecutedEventListenerHttpAdapter;

    /**
     * Handles HTTP PATCH requests with a JSON Patch payload. Routes the requests based on the
     * the operations requested in the patch. In this implementation, one HTTP Patch request is
     * mapped to at most one domain event.
     *
     * @param taskId the local (i.e., implementation-specific) identifier of the task to the patched;
     *               this identifier is extracted from the task's URI
     * @param payload the reuqested patch for this task
     * @return 200 OK and a representation of the task after processing the event; 404 Not Found if
     * the request URI does not match any task; 400 Bad Request if the request is invalid
     */
    @PatchMapping(path = "/tasks/{taskId}", consumes = {JSON_PATCH_MEDIA_TYPE})
    public ResponseEntity<String> dispatchTaskEvents(@PathVariable("taskId") String taskId,
            @RequestBody JsonNode payload) {
        try {
            // Throw an exception if the JSON Patch format is invalid. This call is only used to
            // validate the JSON PATCH syntax.
            JsonPatch.fromJson(payload);

            // Check for known events and route the events to appropriate listeners
            TaskJsonPatchRepresentation representation = new TaskJsonPatchRepresentation(payload);
            Optional<Task.Status> status = representation.extractFirstTaskStatusChange();

            TaskEventListener listener = null;

            // Route events related to task status changes
            if (status.isPresent()) {
                LOGGER.info("Handle Task Status Event: " + status.get().name());
                switch (status.get()) {
                    case ASSIGNED:
                        listener = taskAssignedEventListenerHttpAdapter;
                        break;
                    case RUNNING:
                        listener = taskStartedEventListenerHttpAdapter;
                        break;
                    case EXECUTED:
                        listener = taskExecutedEventListenerHttpAdapter;
                        break;
                }
            }

            if (listener == null) {
                // The HTTP PATCH request is valid, but the patch does not match any known event
                throw new UnknownEventException();
            }

            Task task = listener.handleTaskEvent(taskId, payload);

            // Add the content type as a response header
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add(HttpHeaders.CONTENT_TYPE, TaskJsonRepresentation.MEDIA_TYPE);

            return new ResponseEntity<>(TaskJsonRepresentation.serialize(task), responseHeaders,
                HttpStatus.OK);
        } catch (TaskNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (IOException | RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}

package ch.unisg.tapastasks.tasks.adapter.in.messaging.http;

import ch.unisg.tapascommon.tasks.adapter.common.formats.TaskJsonPatchRepresentation;
import ch.unisg.tapastasks.tasks.application.handler.TaskAssignedHandler;
import ch.unisg.tapastasks.tasks.application.port.in.TaskAssignedEvent;
import ch.unisg.tapascommon.tasks.domain.Task;
import ch.unisg.tapascommon.tasks.domain.Task.TaskId;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Listener for task assigned events. A task assigned event corresponds to a JSON Patch that attempts
 * to change the task's status to ASSIGNED and may also add/replace a service provider (i.e., to what
 * group the task was assigned). This implementation does not impose that a task assigned event
 * includes the service provider (i.e., can be null).
 *
 * See also {@link TaskAssignedEvent}, {@link Task}, and {@link TaskEventHttpDispatcher}.
 */
@RequiredArgsConstructor
@Component
public class TaskAssignedEventListenerHttpAdapter extends TaskEventListener {

    @Autowired
    private final TaskAssignedHandler taskAssignedHandler;

    /**
     * Handles the task assigned event.
     *
     * @param taskId the identifier of the task for which an event was received
     * @param payload the JSON Patch payload of the HTTP PATCH request received for this task
     * @return
     */
    public Task handleTaskEvent(String taskId, JsonNode payload) {
        var representation = new TaskJsonPatchRepresentation(payload);
        var serviceProvider = representation.extractFirstServiceProviderChange();
        var taskAssignedEvent = new TaskAssignedEvent(new TaskId(taskId), serviceProvider);
        return taskAssignedHandler.handleTaskAssigned(taskAssignedEvent);
    }
}

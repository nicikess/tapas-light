package ch.unisg.tapastasks.tasks.adapter.in.messaging.http;

import ch.unisg.tapascommon.tasks.adapter.common.formats.TaskJsonPatchRepresentation;
import ch.unisg.tapastasks.tasks.application.handler.TaskStartedHandler;
import ch.unisg.tapastasks.tasks.application.port.in.TaskStartedEvent;
import ch.unisg.tapascommon.tasks.domain.Task;
import ch.unisg.tapascommon.tasks.domain.Task.TaskId;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Listener for task started events. A task started event corresponds to a JSON Patch that attempts
 * to change the task's status to RUNNING and may also add/replace a service provider. This
 * implementation does not impose that a task started event includes the service provider (i.e.,
 * can be null).
 *
 * See also {@link TaskStartedEvent}, {@link Task}, and {@link TaskEventHttpDispatcher}.
 */
@RequiredArgsConstructor
@Component
public class TaskStartedEventListenerHttpAdapter extends TaskEventListener {

    @Autowired
    private final TaskStartedHandler taskStartedHandler;

    public Task handleTaskEvent(String taskId, JsonNode payload) {
        var representation = new TaskJsonPatchRepresentation(payload);
        var serviceProvider = representation.extractFirstServiceProviderChange();
        var taskStartedEvent = new TaskStartedEvent(new TaskId(taskId), serviceProvider);
        return taskStartedHandler.handleTaskStarted(taskStartedEvent);
    }
}

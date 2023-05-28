package ch.unisg.tapastasks.tasks.adapter.in.messaging.http;

import ch.unisg.tapascommon.tasks.adapter.common.formats.TaskJsonPatchRepresentation;
import ch.unisg.tapastasks.tasks.application.handler.TaskExecutedHandler;
import ch.unisg.tapastasks.tasks.application.port.in.TaskExecutedEvent;
import ch.unisg.tapascommon.tasks.domain.Task;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Listener for task executed events. A task executed event corresponds to a JSON Patch that attempts
 * to change the task's status to EXECUTED, may add/replace a service provider, and may also add an
 * output result. This implementation does not impose that a task executed event includes either the
 * service provider or an output result (i.e., both can be null).
 *
 * See also {@link TaskExecutedEvent}, {@link Task}, and {@link TaskEventHttpDispatcher}.
 */
@RequiredArgsConstructor
@Component
public class TaskExecutedEventListenerHttpAdapter extends TaskEventListener {

    @Autowired
    private final TaskExecutedHandler taskExecutedHandler;

    public Task handleTaskEvent(String taskId, JsonNode payload) {
        var representation = new TaskJsonPatchRepresentation(payload);

        var serviceProvider = representation.extractFirstServiceProviderChange();
        var outputData = representation.extractFirstOutputDataAddition();

        var taskExecutedEvent = new TaskExecutedEvent(
            new Task.TaskId(taskId),
            serviceProvider, outputData
        );

        return taskExecutedHandler.handleTaskExecuted(taskExecutedEvent);
    }
}

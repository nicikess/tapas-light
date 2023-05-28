package ch.unisg.tapastasks.tasks.adapter.in.messaging.http;

import ch.unisg.tapascommon.tasks.domain.Task;
import ch.unisg.tapastasks.tasks.domain.TaskNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Abstract class that handles events specific to a Task. Events are received via an HTTP PATCH
 * request for a given task and dispatched to Task event listeners (see {@link TaskEventHttpDispatcher}).
 * Each listener must implement the abstract method {@link #handleTaskEvent(String, JsonNode)}, which
 * may require additional event-specific validations.
 */
public abstract class TaskEventListener {

    /**
     * This abstract method handles a task event and returns the task after the event was handled.
     *
     * @param taskId the identifier of the task for which an event was received
     * @param payload the JSON Patch payload of the HTTP PATCH request received for this task
     * @return the task for which the HTTP PATCH request is handled
     * @throws TaskNotFoundException
     */
    public abstract Task handleTaskEvent(String taskId, JsonNode payload) throws TaskNotFoundException;
}

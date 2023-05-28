package ch.unisg.tapasexecutorbase.executor.application.service;

import ch.unisg.tapascommon.configuration.ServiceConfiguration;
import ch.unisg.tapascommon.tasks.adapter.common.formats.TaskJsonRepresentation;
import ch.unisg.tapascommon.tasks.domain.Task;
import ch.unisg.tapasexecutorbase.executor.application.port.in.ExecuteTaskCommand;
import ch.unisg.tapasexecutorbase.executor.application.port.in.ExecuteTaskUseCase;
import ch.unisg.tapasexecutorbase.executor.application.port.out.ExecutorStateChangedEventPort;
import ch.unisg.tapasexecutorbase.executor.application.port.out.TaskUpdatedEventPort;
import ch.unisg.tapasexecutorbase.executor.config.ExecutorConfig;
import ch.unisg.tapasexecutorbase.executor.domain.ExecutorStateChangedEvent;
import ch.unisg.tapasexecutorbase.executor.domain.TaskUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExecuteTaskBaseService implements ExecuteTaskUseCase {

    private static final Logger LOGGER = LogManager.getLogger(ExecuteTaskBaseService.class);

    private final ExecutorConfig executorConfig;
    private final ExecutorStateChangedEventPort executorStateChangedEventPort;
    private final TaskUpdatedEventPort taskUpdatedEventPort;

    public void updateExecutorState(String state) {
        LOGGER.info("Update Executor State to: " + state);
        executorStateChangedEventPort.publishExecutorStateChangedEvent(
                new ExecutorStateChangedEvent(executorConfig.getExecutorId(), state)
        );
    }

    public void updateTaskStatus(Task task) {
        LOGGER.info("Update internal Task Status of " +
                task.getTaskId().getValue() + " to " + task.getTaskStatus().getValue());
        var taskRepresentation = new TaskJsonRepresentation(task);
        var ownTaskUri = ServiceConfiguration.getTaskListAddress() + "/tasks/" + taskRepresentation.getTaskId();
        var updateTaskEvent = new TaskUpdatedEvent(
                taskRepresentation.getTaskId(),
                taskRepresentation.getTaskStatus(),
                taskRepresentation.getOutputData(),
                ownTaskUri);
        taskUpdatedEventPort.updateTaskStatusEvent(updateTaskEvent);

        if (!taskRepresentation.getOriginalTaskUri().isEmpty()) {
            LOGGER.info("Update external Task Status of " +
                    task.getTaskId().getValue() + " to " + task.getTaskStatus().getValue());
            var updateDelegatedTaskEvent = new TaskUpdatedEvent(
                    taskRepresentation.getTaskId(),
                    taskRepresentation.getTaskStatus(),
                    taskRepresentation.getOutputData(),
                    taskRepresentation.getOriginalTaskUri());
            taskUpdatedEventPort.updateTaskStatusEvent(updateDelegatedTaskEvent);
        }
    }

    @Override
    public void executeTask(ExecuteTaskCommand command) { }
}

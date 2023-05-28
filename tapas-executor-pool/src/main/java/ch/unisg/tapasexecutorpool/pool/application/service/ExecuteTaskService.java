package ch.unisg.tapasexecutorpool.pool.application.service;

import ch.unisg.tapascommon.configuration.ServiceConfiguration;
import ch.unisg.tapasexecutorpool.pool.application.port.in.ExecuteTaskCommand;
import ch.unisg.tapasexecutorpool.pool.application.port.in.ExecuteTaskUseCase;
import ch.unisg.tapasexecutorpool.pool.application.port.out.*;
import ch.unisg.tapasexecutorpool.pool.domain.ExecutorPool;
import ch.unisg.tapascommon.tasks.domain.Task;
import ch.unisg.tapasexecutorpool.pool.domain.TaskAssignment;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;

@RequiredArgsConstructor
@Component
@Transactional
public class ExecuteTaskService implements ExecuteTaskUseCase {

    private static final Logger LOGGER = LogManager.getLogger(ExecuteTaskService.class);

    private final TaskAssignedEventPort taskAssignedEventPort;
    private final ForwardTaskToExecutorEventPort forwardTaskToExecutorEventPort;

    private final AddTaskAssignmentToRepositoryPort addTaskAssignmentToRepositoryPort;
    private final TaskAssignmentsLock taskAssignmentsLock;

    @Override
    public Task executeTask(ExecuteTaskCommand command) {
        var task = command.getTask();
        LOGGER.info("Executing new Task: " + task);

        var pool = ExecutorPool.getTapasExecutorPool();
        var executorOptional = pool.retrieveAvailableExecutorByTaskType(task.getTaskType());

        if (executorOptional.isPresent()) {
            var executor =  executorOptional.get();
            var taskId = task.getTaskId().getValue();

            task.setTaskStatus(new Task.TaskStatus(Task.Status.ASSIGNED));
            taskAssignedEventPort.handleTaskAssignedEvent(
                    new TaskAssignedEvent(
                            "tapas-group4",
                            ServiceConfiguration.getTaskListAddress() + "/tasks/" + taskId
                    )
            );

            taskAssignmentsLock.lockTaskAssignments();
            addTaskAssignmentToRepositoryPort.addTaskAssignment(
                    new TaskAssignment(task.getTaskId(), executor.getExecutorId())
            );
            taskAssignmentsLock.releaseTaskAssignments();

            forwardTaskToExecutorEventPort.forwardTaskToExecutorEvent(
                    new ForwardTaskToExecutorEvent(task, executor)
            );
        }

        return task;
    }
}

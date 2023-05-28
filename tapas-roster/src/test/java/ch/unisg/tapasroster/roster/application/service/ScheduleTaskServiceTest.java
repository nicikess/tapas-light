package ch.unisg.tapasroster.roster.application.service;

import ch.unisg.tapascommon.pool.domain.Executor;
import ch.unisg.tapascommon.tasks.domain.Task;
import ch.unisg.tapasroster.roster.application.port.in.ScheduleTaskCommand;
import ch.unisg.tapasroster.roster.application.port.out.*;
import ch.unisg.tapasroster.roster.domain.ExecutorRegistry;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.BDDMockito.*;

public class ScheduleTaskServiceTest {

    private final QueryAvailableExecutorsFromPoolEventHandler queryAvailableExecutorsFromPoolEventHandler
            = Mockito.mock(QueryAvailableExecutorsFromPoolEventHandler.class);

    private final ForwardTaskToExecutorPoolEventPort forwardTaskToExecutorPoolEventPort
            = Mockito.mock(ForwardTaskToExecutorPoolEventPort.class);

    private final ForwardTaskToAuctionHouseEventPort forwardTaskToAuctionHouseEventPort
            = Mockito.mock(ForwardTaskToAuctionHouseEventPort.class);

    private final ScheduleTaskService scheduleTaskService = new ScheduleTaskService(
            queryAvailableExecutorsFromPoolEventHandler,
            forwardTaskToExecutorPoolEventPort,
            forwardTaskToAuctionHouseEventPort
    );

    @Test
    void scheduleTaskForwardToPool() {

        givenAnInternalComputationExecutorInRegistry();

        var taskToSchedule = Task.createTaskWithNameAndType(
                new Task.TaskName("test-task-computation"),
                new Task.TaskType(Task.Type.COMPUTATION.name())
        );
        var scheduleTaskCommand = new ScheduleTaskCommand(taskToSchedule);

        scheduleTaskService.scheduleTask(scheduleTaskCommand);

        then(queryAvailableExecutorsFromPoolEventHandler)
                .should(times(1))
                .retrieveAvailableExecutors(any(QueryAvailableExecutorsFromPoolEvent.class));
        then(forwardTaskToExecutorPoolEventPort)
                .should(times(1))
                .forwardTaskToExecutorPoolEvent(eq(new ForwardTaskToExecutorPoolEvent(taskToSchedule)));
        then(forwardTaskToAuctionHouseEventPort).shouldHaveZeroInteractions();
    }

    @Test
    void scheduleTaskForwardToAuctionHouse() {

        givenAnInternalComputationExecutorInRegistry();

        var taskToSchedule = Task.createTaskWithNameAndType(
                new Task.TaskName("test-task-auction"),
                new Task.TaskType(Task.Type.RANDOMTEXT.name())
        );
        var scheduleTaskCommand = new ScheduleTaskCommand(taskToSchedule);

        scheduleTaskService.scheduleTask(scheduleTaskCommand);

        then(queryAvailableExecutorsFromPoolEventHandler)
                .should(times(1))
                .retrieveAvailableExecutors(any(QueryAvailableExecutorsFromPoolEvent.class));
        then(forwardTaskToAuctionHouseEventPort)
                .should(times(1))
                .forwardTaskToAuctionHouseEvent(any(ForwardTaskToAuctionHouseEvent.class));
        then(forwardTaskToExecutorPoolEventPort).shouldHaveZeroInteractions();
    }

    void givenAnInternalComputationExecutorInRegistry() {

        var executorId = "test-executor-id";
        var executorName = "test-executor-name";
        var executorType = Task.Type.COMPUTATION;
        var executorAddress = "http://127.0.0.1:8085";
        var executorPoolName = "test-pool";

        var executor = new Executor(
                new Executor.ExecutorId(executorId),
                new Executor.ExecutorName(executorName),
                new Executor.ExecutorType(executorType),
                new Executor.ExecutorAddress(executorAddress),
                new Executor.ExecutorState(Executor.State.IDLE),
                new Executor.ExecutorPoolName(executorPoolName)
        );

        var executorRegistry = ExecutorRegistry.getInstance();
        executorRegistry.clearExecutors();
        executorRegistry.addExecutor(executor);
    }
}

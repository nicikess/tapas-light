package ch.unisg.tapasroster.roster.application.service;

import ch.unisg.tapascommon.tasks.domain.Task;
import ch.unisg.tapasroster.roster.application.port.in.ScheduleTaskCommand;
import ch.unisg.tapasroster.roster.application.port.in.ScheduleTaskUseCase;
import ch.unisg.tapasroster.roster.application.port.out.ForwardTaskToAuctionHouseEventPort;
import ch.unisg.tapasroster.roster.application.port.out.ForwardTaskToExecutorPoolEventPort;
import ch.unisg.tapasroster.roster.application.port.out.QueryAvailableExecutorsFromPoolEvent;
import ch.unisg.tapasroster.roster.application.port.out.QueryAvailableExecutorsFromPoolEventHandler;
import ch.unisg.tapasroster.roster.application.port.out.ForwardTaskToAuctionHouseEvent;
import ch.unisg.tapasroster.roster.application.port.out.ForwardTaskToExecutorPoolEvent;
import ch.unisg.tapasroster.roster.domain.ExecutorRegistry;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;

@RequiredArgsConstructor
@Component
@Transactional
public class ScheduleTaskService implements ScheduleTaskUseCase {

    private static final Logger LOGGER = LogManager.getLogger(ScheduleTaskService.class);

    private final QueryAvailableExecutorsFromPoolEventHandler queryAvailableExecutorsFromPoolEventHandler;
    private final ForwardTaskToExecutorPoolEventPort forwardTaskToExecutorPoolEventPort;
    private final ForwardTaskToAuctionHouseEventPort forwardTaskToAuctionHouseEventPort;

    private final Random random = new Random();

    @Override
    public boolean scheduleTask(ScheduleTaskCommand command) {
        var task = command.getTask();
        LOGGER.info("Schedule new Task: " + task);

        queryAvailableExecutorsFromExecutorPool();
        var registry = ExecutorRegistry.getInstance();

        var taskType = task.getTaskTypeEnum();
        if (registry.hasExecutorWithTaskType(taskType)) {
            forwardTaskToExecutorPool(task);
        } else {
            forwardTaskToAuctionHouse(task);
        }

        return true;
    }

    private void queryAvailableExecutorsFromExecutorPool() {
        var event = new QueryAvailableExecutorsFromPoolEvent();
        queryAvailableExecutorsFromPoolEventHandler.retrieveAvailableExecutors(event);
    }

    private void forwardTaskToExecutorPool(Task task) {
        LOGGER.info("Forward Task to Executor Pool");
        var eventExecutorPool = new ForwardTaskToExecutorPoolEvent(task);
        forwardTaskToExecutorPoolEventPort.forwardTaskToExecutorPoolEvent(eventExecutorPool);
    }

    private void forwardTaskToAuctionHouse(Task task) {
        LOGGER.info("Forward Task to Auction House");
        var deadline = Timestamp.valueOf(LocalDateTime.now().plusSeconds(random.nextInt(10) + 10));
        LOGGER.info("Auction Deadline is " + deadline);
        var eventAuctionHouse = new ForwardTaskToAuctionHouseEvent(task, deadline);
        forwardTaskToAuctionHouseEventPort.forwardTaskToAuctionHouseEvent(eventAuctionHouse);
    }
}

package ch.unisg.tapasexecutorrobot.executor.application.service;

import ch.unisg.tapasexecutorbase.executor.application.port.in.ExecuteTaskCommand;
import ch.unisg.tapasexecutorbase.executor.application.port.in.ExecuteTaskUseCase;
import ch.unisg.tapasexecutorbase.executor.application.port.out.ExecutorStateChangedEventPort;
import ch.unisg.tapasexecutorbase.executor.application.port.out.TaskUpdatedEventPort;
import ch.unisg.tapasexecutorbase.executor.application.service.ExecuteTaskBaseService;
import ch.unisg.tapasexecutorbase.executor.config.ExecutorConfig;
import ch.unisg.tapasexecutorrobot.executor.domain.Cherrybot;
import ch.unisg.tapascommon.tasks.domain.Task;
import ch.unisg.tapasexecutorrobot.executor.domain.CherrybotWot;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Primary
@Component
public class ExecuteTaskService implements ExecuteTaskUseCase {

    private static final Logger LOGGER = LogManager.getLogger(ExecuteTaskService.class);

    private final ExecutorConfig executorConfig;
    private final ExecutorStateChangedEventPort executorStateChangedEventPort;
    private final TaskUpdatedEventPort taskUpdatedEventPort;

    private void moveRobotRest() {
        LOGGER.info("Start moving Robot via REST");
        var robot = new Cherrybot();
        if (!robot.postOperator()) {
            LOGGER.warn("Failed to register operator");
            return;
        }
        if (!robot.putTcp()) {
            LOGGER.warn("Failed to move to tcp position");
        }
        if (!robot.postInitialize()) {
            LOGGER.warn("Failed to move to initialize position");
        }
        if (!robot.deleteOperator()) {
            LOGGER.warn("Failed to delete operator");
        }
        LOGGER.info("Finished moving Robot via REST");
    }

    private void moveRobotWot() {
        LOGGER.info("Start moving Robot via WoT");
        var robot = new CherrybotWot();
        if (!robot.registerOperator()) {
            LOGGER.warn("Failed to register operator");
            return;
        }
        if (!robot.tcpSetTarget()) {
            LOGGER.warn("Failed to move to tcp position");
        }
        if (!robot.initialize()) {
            LOGGER.warn("Failed to move to initialize position");
        }
        if (!robot.removeOperator()) {
            LOGGER.warn("Failed to delete operator");
        }
        LOGGER.info("Finished moving Robot via WoT");
    }

    private void deleteOperator(String operatorToken) {
        var robot = new Cherrybot();
        robot.setOperatorToken(operatorToken);
        if (!robot.deleteOperator()) {
            LOGGER.warn("Failed to delete operator");
        }
    }

    @Override
    public void executeTask(ExecuteTaskCommand command) {
        var executorBaseService = new ExecuteTaskBaseService(
                executorConfig, executorStateChangedEventPort, taskUpdatedEventPort
        );

        executorBaseService.updateExecutorState("BUSY");

        var task = command.getTask();

        task.setTaskStatus(new Task.TaskStatus(Task.Status.RUNNING));
        executorBaseService.updateTaskStatus(task);

        // moveRobotRest();
        moveRobotWot();

        task.setOutputData(new Task.OutputData("COMPLETED"));
        task.setTaskStatus(new Task.TaskStatus(Task.Status.EXECUTED));
        executorBaseService.updateTaskStatus(task);

        executorBaseService.updateExecutorState("IDLE");
    }
}

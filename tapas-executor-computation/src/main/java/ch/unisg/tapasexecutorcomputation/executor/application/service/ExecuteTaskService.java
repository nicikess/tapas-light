package ch.unisg.tapasexecutorcomputation.executor.application.service;

import ch.unisg.tapascommon.tasks.domain.Task;
import ch.unisg.tapasexecutorbase.executor.application.port.in.ExecuteTaskCommand;
import ch.unisg.tapasexecutorbase.executor.application.port.in.ExecuteTaskUseCase;
import ch.unisg.tapasexecutorbase.executor.application.port.out.ExecutorStateChangedEventPort;
import ch.unisg.tapasexecutorbase.executor.application.port.out.TaskUpdatedEventPort;
import ch.unisg.tapasexecutorbase.executor.application.service.ExecuteTaskBaseService;
import ch.unisg.tapasexecutorbase.executor.config.ExecutorConfig;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

@RequiredArgsConstructor
@Primary
@Component
public class ExecuteTaskService implements ExecuteTaskUseCase {

    private static final Logger LOGGER = LogManager.getLogger(ExecuteTaskService.class);

    private static final String JAVASCRIPT_ENGINE_NAME = "JavaScript";

    private final ExecutorConfig executorConfig;
    private final ExecutorStateChangedEventPort executorStateChangedEventPort;
    private final TaskUpdatedEventPort taskUpdatedEventPort;

    private String calculate(String expression) {
        var engine = new ScriptEngineManager().getEngineByName(JAVASCRIPT_ENGINE_NAME);
        try {
            return engine.eval(expression).toString();
        } catch (ScriptException e) {
            return "INVALID EXPRESSION";
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

        var expression = task.getInputData().getValue();
        var result = calculate(expression);

        LOGGER.info("Math Expression: " + expression);
        LOGGER.info("Result: " + result);

        task.setOutputData(new Task.OutputData(result));
        task.setTaskStatus(new Task.TaskStatus(Task.Status.EXECUTED));
        executorBaseService.updateTaskStatus(task);

        executorBaseService.updateExecutorState("IDLE");
    }
}

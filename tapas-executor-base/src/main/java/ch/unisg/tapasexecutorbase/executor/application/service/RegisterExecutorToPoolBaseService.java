package ch.unisg.tapasexecutorbase.executor.application.service;

import ch.unisg.tapascommon.configuration.ServiceConfiguration;
import ch.unisg.tapascommon.communication.WebClient;
import ch.unisg.tapascommon.pool.adapter.common.formats.ExecutorJsonRepresentation;
import ch.unisg.tapasexecutorbase.executor.application.port.in.RegisterExecutorToPoolCommand;
import ch.unisg.tapasexecutorbase.executor.application.port.in.RegisterExecutorToPoolUseCase;
import ch.unisg.tapasexecutorbase.executor.config.ExecutorConfig;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class RegisterExecutorToPoolBaseService implements RegisterExecutorToPoolUseCase {

    private static final Logger LOGGER = LogManager.getLogger(RegisterExecutorToPoolBaseService.class);

    private final ExecutorConfig executorConfig;

    public static boolean registerExecutorToPool(String id, String name, String type, String address) {
        try {
            WebClient.post(
                    ServiceConfiguration.getExecutorPoolAddress() + "/executors/",
                    new ExecutorJsonRepresentation(id, name, type, address).serialize(),
                    ExecutorJsonRepresentation.MEDIA_TYPE
            );
            LOGGER.info("Registered this Executor to Pool");
            return true;
        } catch (IOException | InterruptedException e) {
            LOGGER.warn("Failed to register this Executor to Pool");
            return false;
        }
    }

    @Override
    public boolean registerThisExecutorToPool(RegisterExecutorToPoolCommand command) {
        var executorId = executorConfig.getExecutorId();
        return RegisterExecutorToPoolBaseService.registerExecutorToPool(
                executorId,
                executorConfig.getExecutorName(),
                executorConfig.getExecutorType(),
                ServiceConfiguration.getExecutorAddress(executorId)
        );
    }
}

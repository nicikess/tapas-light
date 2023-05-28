package ch.unisg.tapas.auctionhouse.application.handler;

import ch.unisg.tapas.auctionhouse.application.port.in.TaskWonEvent;
import ch.unisg.tapas.auctionhouse.application.port.in.TaskWonEventHandler;
import ch.unisg.tapascommon.configuration.ServiceConfiguration;
import ch.unisg.tapascommon.communication.WebClient;
import ch.unisg.tapascommon.tasks.adapter.common.formats.TaskJsonRepresentation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Primary
@Component
public class TaskWonHandler implements TaskWonEventHandler {

    private static final Logger LOGGER = LogManager.getLogger(TaskWonHandler.class);

    @Override
    public WonTaskStatus handleAuctionWon(TaskWonEvent event) {
        LOGGER.info("Handling Task Won Event");
        try {
            WebClient.post(
                ServiceConfiguration.getTaskListAddress() + "/tasks",
                event.getTaskJsonRepresentation().serialize(),
                TaskJsonRepresentation.MEDIA_TYPE);
            LOGGER.info("Delegated task forwarded to task list service");
            return WonTaskStatus.OK;
        } catch (IOException | InterruptedException ignored) {
            LOGGER.warn("Failed to forward delegated task");
            return WonTaskStatus.CANNOT_EXECUTE;
        }
    }
}

package ch.unisg.tapas.auctionhouse.adapter.out.messaging.http;

import ch.unisg.tapas.auctionhouse.application.port.out.AuctionWonEventPort;
import ch.unisg.tapas.auctionhouse.domain.AuctionRegistry;
import ch.unisg.tapas.auctionhouse.domain.AuctionWonEvent;
import ch.unisg.tapascommon.communication.WebClient;
import ch.unisg.tapascommon.tasks.adapter.common.formats.TaskJsonRepresentation;
import ch.unisg.tapascommon.tasks.domain.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.http.HttpResponse;

/**
 * This class is a template for sending auction won events via HTTP. This class was created here only
 * as a placeholder, it is up to you to decide how such events should be sent (e.g., via HTTP,
 * WebSub, etc.).
 */
@Component
@Primary
public class AuctionWonEventHttpAdapter implements AuctionWonEventPort {

    private static final Logger LOGGER = LogManager.getLogger(AuctionWonEventHttpAdapter.class);

    private static final String PATH = "/taskwinner";

    @Override
    public void publishAuctionWonEvent(AuctionWonEvent event) {
        LOGGER.info("Publish Auction Won Event to " + event.getWinningBid().getBidderName().getValue());

        var winningBid = event.getWinningBid();

        var auctionId = winningBid.getAuctionId();
        var auctionOptional = AuctionRegistry.getInstance().getAuctionById(auctionId);

        if (auctionOptional.isPresent()) {
            var auction = auctionOptional.get();
            var taskUri = auction.getTaskUri().getValue().toString();
            HttpResponse<String> taskResponse;
            try {
                taskResponse = WebClient.get(taskUri);
            } catch (IOException | InterruptedException e) {
                LOGGER.warn("Failed to retrieve the task from task list");
                return;
            }

            var taskJson = taskResponse.body();
            TaskJsonRepresentation taskRepresentation;
            String delegatedTaskJson;
            try {
                taskRepresentation = TaskJsonRepresentation.fromJson(taskJson);
                taskRepresentation.setOriginalTaskUri(taskUri);
                delegatedTaskJson = taskRepresentation.serialize();
            } catch (JsonProcessingException e) {
                LOGGER.warn("Failed to set delegated Task uri");
                LOGGER.warn(e);
                return;
            }

            var auctionHouseUri = winningBid.getBidderAuctionHouseUri().getValue().toString();

            if (!auctionHouseUri.contains(PATH)) {
                auctionHouseUri += PATH;
            }
            auctionHouseUri = WebClient.normalizeUrl(auctionHouseUri);
            LOGGER.info("Send Task Won Event to " + auctionHouseUri);

            try {
                var response = WebClient.post(auctionHouseUri, delegatedTaskJson, TaskJsonRepresentation.MEDIA_TYPE);
                if (WebClient.checkResponseStatusCode(response)) {
                    LOGGER.info("Successfully notified the winning auction house");
                } else {
                    LOGGER.warn("Failed to notify the winning auction house");
                }
            } catch (IOException | InterruptedException ignored) {
                LOGGER.warn("Failed to notify the winning auction house");
            }
        } else {
            LOGGER.warn("Failed to retrieve the auction from the auction registry");
        }
    }
}

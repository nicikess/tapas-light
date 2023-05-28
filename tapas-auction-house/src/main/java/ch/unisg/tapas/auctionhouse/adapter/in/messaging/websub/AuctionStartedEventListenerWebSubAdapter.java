package ch.unisg.tapas.auctionhouse.adapter.in.messaging.websub;

import ch.unisg.tapas.auctionhouse.adapter.common.formats.AuctionJsonRepresentation;
import ch.unisg.tapas.auctionhouse.application.port.in.AuctionStartedEvent;
import ch.unisg.tapas.auctionhouse.application.port.in.AuctionStartedEventHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.springframework.stereotype.Component;
import java.util.ArrayList;

@RequiredArgsConstructor
@Component
public class AuctionStartedEventListenerWebSubAdapter {

    private static final Logger LOGGER = LogManager.getLogger(AuctionStartedEventListenerWebSubAdapter.class);

    private final AuctionStartedEventHandler auctionStartedEventHandler;

    public void handleAuctionStartedEvent(String openAuctionsJson) {
        var openAuctions = new ArrayList<AuctionJsonRepresentation>();
        var openAuctionsJsonArray = new JSONArray(openAuctionsJson);
        for (var auction : openAuctionsJsonArray) {
            try {
                openAuctions.add(AuctionJsonRepresentation.fromJsonString(auction.toString()));
                LOGGER.info("Parsed new open auction from AuctionStartedEvent via WebSub");
            } catch (JsonProcessingException e) {
                LOGGER.warn("Failed to parse open auction from AuctionStartedEvent via WebSub");
            }
        }

        for (var auction : openAuctions) {
            var event = new AuctionStartedEvent(auction.deserialize());
            auctionStartedEventHandler.handleAuctionStartedEvent(event);
        }
    }
}

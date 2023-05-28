package ch.unisg.tapas.auctionhouse.adapter.out.messaging.http;

import ch.unisg.tapas.auctionhouse.adapter.common.formats.BidJsonRepresentation;
import ch.unisg.tapas.auctionhouse.application.port.out.PlaceBidForAuctionCommand;
import ch.unisg.tapas.auctionhouse.application.port.out.PlaceBidForAuctionCommandPort;
import ch.unisg.tapascommon.communication.WebClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Sends the place bid for an auction command via HTTP.
 */
@Component
@Primary
public class PlaceBidForAuctionCommandHttpAdapter implements PlaceBidForAuctionCommandPort {

    private static final Logger LOGGER = LogManager.getLogger(PlaceBidForAuctionCommandHttpAdapter.class);

    private static final String PATH = "/bid";

    @Override
    public void placeBid(PlaceBidForAuctionCommand command) {
        try {
            var auction = command.getAuction();
            var uri = auction.getAuctionHouseUri().getValue() + PATH;
            LOGGER.info("Send Bid to " + uri);
            var response = WebClient.postNonStandarizedEndpoint(
                auction.getAuctionHouseUri().getValue() + PATH,
                BidJsonRepresentation.serialize(command.getBid()),
                BidJsonRepresentation.MEDIA_TYPE
            );

            var status = HttpStatus.valueOf(response.statusCode());
            switch(status) {
                case NO_CONTENT:
                    LOGGER.info("Bid was received and accepted");
                    break;
                case NOT_FOUND:
                    LOGGER.info("There never was an auction with that ID " + auction.getAuctionId().getValue());
                    break;
                case GONE:
                    LOGGER.info("The Auction that was requested has already expired");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

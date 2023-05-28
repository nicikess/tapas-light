package ch.unisg.tapas.auctionhouse.application.handler;

import ch.unisg.tapas.auctionhouse.application.port.in.BidReceivedEvent;
import ch.unisg.tapas.auctionhouse.application.port.in.BidReceivedEventHandler;
import ch.unisg.tapas.auctionhouse.domain.AuctionRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class BidReceivedHandler implements BidReceivedEventHandler {

    private static final Logger LOGGER = LogManager.getLogger(BidReceivedHandler.class);

    @Override
    public BidStatus handleBidReceivedEvent(BidReceivedEvent event) {
        var registry = AuctionRegistry.getInstance();

        var bidRepresentation = event.getBidJsonRepresentation();
        var bid = bidRepresentation.deserialize();
        var auctionOptional = registry.getAuctionById(bid.getAuctionId());

        if (auctionOptional.isPresent()) {
            var auction = auctionOptional.get();

            if (auction.isOpen()) {
                registry.placeBid(bid);

                LOGGER.info("Received bid from " + bid.getBidderName().getValue());
                return BidStatus.OK;
            }

            LOGGER.info("Received bid was too late from " + bid.getBidderName().getValue());
            return BidStatus.TOO_LATE;
        }

        LOGGER.info("No auction found for received bid from " + bid.getBidderName().getValue());
        return BidStatus.NO_AUCTION;
    }
}

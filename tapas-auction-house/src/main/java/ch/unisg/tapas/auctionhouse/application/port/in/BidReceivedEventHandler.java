package ch.unisg.tapas.auctionhouse.application.port.in;

public interface BidReceivedEventHandler {

    enum BidStatus {
        OK, TOO_LATE, NO_AUCTION
    }

    BidStatus handleBidReceivedEvent(BidReceivedEvent event);
}

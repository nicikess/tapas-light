package ch.unisg.tapas.auctionhouse.application.port.in;

public interface AuctionStartedEventHandler {
    boolean handleAuctionStartedEvent(AuctionStartedEvent auctionStartedEvent);
}

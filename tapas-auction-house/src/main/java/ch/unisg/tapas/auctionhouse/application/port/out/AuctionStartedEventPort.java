package ch.unisg.tapas.auctionhouse.application.port.out;

import ch.unisg.tapas.auctionhouse.domain.AuctionStartedEvent;

/**
 * Port for sending out auction started events
 */
public interface AuctionStartedEventPort {

    void publishAuctionStartedEvent(AuctionStartedEvent event);
}

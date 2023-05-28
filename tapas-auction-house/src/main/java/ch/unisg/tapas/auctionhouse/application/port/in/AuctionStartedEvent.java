package ch.unisg.tapas.auctionhouse.application.port.in;

import ch.unisg.tapas.auctionhouse.domain.Auction;
import ch.unisg.tapas.common.SelfValidating;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotNull;

/**
 * Event that notifies this auction house that an auction was started by another auction house.
 */
@EqualsAndHashCode(callSuper = true)
@Value
public class AuctionStartedEvent extends SelfValidating<AuctionStartedEvent> {
    @NotNull
    Auction auction;

    public AuctionStartedEvent(Auction auction) {
        this.auction = auction;
        this.validateSelf();
    }
}

package ch.unisg.tapas.auctionhouse.application.port.out;

import ch.unisg.tapas.auctionhouse.domain.Auction;
import ch.unisg.tapas.auctionhouse.domain.Bid;
import ch.unisg.tapas.common.SelfValidating;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotNull;

/**
 * Command to place a bid for a given auction.
 */
@EqualsAndHashCode(callSuper = true)
@Value
public class PlaceBidForAuctionCommand extends SelfValidating<PlaceBidForAuctionCommand> {
    @NotNull
    Auction auction;
    @NotNull
    Bid bid;

    public PlaceBidForAuctionCommand(Auction auction, Bid bid) {
        this.auction = auction;
        this.bid = bid;
        this.validateSelf();
    }
}

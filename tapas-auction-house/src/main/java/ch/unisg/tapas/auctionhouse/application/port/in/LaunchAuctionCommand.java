package ch.unisg.tapas.auctionhouse.application.port.in;

import ch.unisg.tapas.auctionhouse.domain.Auction;
import ch.unisg.tapas.common.SelfValidating;
import lombok.Value;

import javax.validation.constraints.NotNull;

/**
 * Command for launching an auction in this auction house.
 */
@Value
public class LaunchAuctionCommand extends SelfValidating<LaunchAuctionCommand> {
    @NotNull
    private final Auction.AuctionedTaskUri taskUri;

    @NotNull
    private final Auction.AuctionedTaskType taskType;

    private final Auction.AuctionDeadline deadline;

    /**
     * Constructs the launch action command.
     *
     * @param taskUri the URI of the auctioned task
     * @param taskType the type of the auctioned task
     * @param deadline the deadline by which the auction should receive bids (can be null if none)
     */
    public LaunchAuctionCommand(Auction.AuctionedTaskUri taskUri, Auction.AuctionedTaskType taskType,
                                Auction.AuctionDeadline deadline) {
        this.taskUri = taskUri;
        this.taskType = taskType;
        this.deadline = deadline;

        this.validateSelf();
    }
}

package ch.unisg.tapas.auctionhouse.application.service;

import ch.unisg.tapas.auctionhouse.application.port.in.LaunchAuctionCommand;
import ch.unisg.tapas.auctionhouse.application.port.in.LaunchAuctionUseCase;
import ch.unisg.tapas.auctionhouse.application.port.out.AuctionWonEventPort;
import ch.unisg.tapas.auctionhouse.application.port.out.AuctionStartedEventPort;
import ch.unisg.tapas.auctionhouse.application.port.out.TaskAssignedEvent;
import ch.unisg.tapas.auctionhouse.application.port.out.TaskAssignedEventPort;
import ch.unisg.tapas.auctionhouse.domain.*;
import ch.unisg.tapascommon.configuration.ServiceConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Service that implements the {@link LaunchAuctionUseCase} to start an auction. If a deadline is
 * specified for the auction, the service automatically closes the auction at the deadline. If a
 * deadline is not specified, the service closes the auction after 10s by default.
 */
@Component
public class StartAuctionService implements LaunchAuctionUseCase {
    private static final Logger LOGGER = LogManager.getLogger(StartAuctionService.class);

    private final static int DEFAULT_AUCTION_DEADLINE_SECONDS = 10;

    // Event port used to publish an auction started event
    private final AuctionStartedEventPort auctionStartedEventPort;
    // Event port used to publish an auction won event
    private final AuctionWonEventPort auctionWonEventPort;
    private final TaskAssignedEventPort taskAssignedEventPort;

    private final ScheduledExecutorService service;
    private final AuctionRegistry auctions;

    public StartAuctionService(
        AuctionStartedEventPort auctionStartedEventPort,
        AuctionWonEventPort auctionWonEventPort,
        TaskAssignedEventPort taskAssignedEventPort
    ) {
        this.auctionStartedEventPort = auctionStartedEventPort;
        this.auctionWonEventPort = auctionWonEventPort;
        this.auctions = AuctionRegistry.getInstance();
        this.service = Executors.newScheduledThreadPool(1);
        this.taskAssignedEventPort = taskAssignedEventPort;
    }

    /**
     * Launches an auction.
     *
     * @param command the domain command used to launch the auction (see {@link LaunchAuctionCommand})
     * @return the launched auction
     */
    @Override
    public Auction launchAuction(LaunchAuctionCommand command) {
        Auction.AuctionDeadline deadline = (command.getDeadline() == null) ?
            new Auction.AuctionDeadline(Timestamp.valueOf(
                LocalDateTime.now().plusSeconds(DEFAULT_AUCTION_DEADLINE_SECONDS))
            ) : command.getDeadline();

        // Create a new auction and add it to the auction registry
        Auction auction = new Auction(new Auction.AuctionHouseUri(
            URI.create(ServiceConfiguration.getAuctionHouseAddress())),
            command.getTaskUri(), command.getTaskType(), deadline);
        auctions.addAuction(auction);

        LOGGER.info("Auction #" + auction.getAuctionId().getValue() + " for task "
            + auction.getTaskUri().getValue() + " started");

        // Schedule the closing of the auction at the deadline
        var deadlineSeconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), deadline.getValue().toLocalDateTime());
        if (deadlineSeconds <= 0) {
            LOGGER.info("Auction deadline is in the past. Setting the deadline to default: "
                + DEFAULT_AUCTION_DEADLINE_SECONDS + " seconds");
            deadlineSeconds = DEFAULT_AUCTION_DEADLINE_SECONDS;
        }
        service.schedule(new CloseAuctionTask(auction.getAuctionId()), deadlineSeconds, TimeUnit.SECONDS);

        // Publish an auction started event
        AuctionStartedEvent auctionStartedEvent = new AuctionStartedEvent(auction);
        auctionStartedEventPort.publishAuctionStartedEvent(auctionStartedEvent);

        return auction;
    }

    /**
     * This task closes the auction at the deadline and selects a winner if any bids were placed. It
     * also sends out associated events and commands.
     */
    private class CloseAuctionTask implements Runnable {
        Auction.AuctionId auctionId;

        public CloseAuctionTask(Auction.AuctionId auctionId) {
            this.auctionId = auctionId;
        }

        @Override
        public void run() {
            Optional<Auction> auctionOpt = auctions.getAuctionById(auctionId);

            if (auctionOpt.isPresent()) {
                Auction auction = auctionOpt.get();
                Optional<Bid> bid = auction.selectBid();

                // Close the auction
                auction.close();

                if (bid.isPresent()) {
                    // Notify the bidder
                    var bidderName = bid.get().getBidderName().getValue();
                    var taskUri = auction.getTaskUri().getValue().toString();

                    LOGGER.info("Auction #" + auction.getAuctionId().getValue() + " for task "
                            + taskUri + " won by " + bidderName);

                    taskAssignedEventPort.handleTaskAssignedEvent(
                        new TaskAssignedEvent(
                            bidderName,
                            taskUri
                        )
                    );

                    // Send an auction won event for the winning bid
                    auctionWonEventPort.publishAuctionWonEvent(new AuctionWonEvent(bid.get()));
                } else {
                    LOGGER.info("Auction #" + auction.getAuctionId().getValue() + " ended with no bids for task "
                            + auction.getTaskUri().getValue());
                }
            }
        }
    }
}

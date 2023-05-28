package ch.unisg.tapas.auctionhouse.application.port.in;

public interface TaskWonEventHandler {

    enum WonTaskStatus {
        OK, CANNOT_EXECUTE
    }

    WonTaskStatus handleAuctionWon(TaskWonEvent event);
}

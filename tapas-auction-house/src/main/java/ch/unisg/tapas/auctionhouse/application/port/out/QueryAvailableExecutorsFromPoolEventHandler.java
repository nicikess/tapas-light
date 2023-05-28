package ch.unisg.tapas.auctionhouse.application.port.out;

public interface QueryAvailableExecutorsFromPoolEventHandler {
    boolean retrieveAvailableExecutors(QueryAvailableExecutorsFromPoolEvent event);
}

package ch.unisg.tapas.auctionhouse.application.port.out;

public interface PlaceBidForAuctionCommandPort {
    void placeBid(PlaceBidForAuctionCommand command);
}

package ch.unisg.tapas.auctionhouse.application.port.in;

import ch.unisg.tapas.auctionhouse.domain.Auction;

public interface LaunchAuctionUseCase {

    Auction launchAuction(LaunchAuctionCommand command);
}

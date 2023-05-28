package ch.unisg.tapas.auctionhouse.application.port.in;

import ch.unisg.tapas.auctionhouse.domain.DiscoveredAuctionHouseInfo;

public interface ProvideDiscoveredAuctionHousesUseCase {

    DiscoveredAuctionHouseInfo[] provideDiscoveredAuctionHouses(ProvideDiscoveredAuctionHousesCommand command);
}

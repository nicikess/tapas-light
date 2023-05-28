package ch.unisg.tapas.auctionhouse.application.service;

import ch.unisg.tapas.auctionhouse.application.port.in.ProvideDiscoveredAuctionHousesCommand;
import ch.unisg.tapas.auctionhouse.application.port.in.ProvideDiscoveredAuctionHousesUseCase;
import ch.unisg.tapas.auctionhouse.domain.DiscoveredAuctionHouseInfo;
import ch.unisg.tapas.auctionhouse.domain.DiscoveredAuctionHouseRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProvideDiscoveredAuctionHousesService implements ProvideDiscoveredAuctionHousesUseCase {

    @Override
    public DiscoveredAuctionHouseInfo[] provideDiscoveredAuctionHouses(ProvideDiscoveredAuctionHousesCommand command) {
        var registry = DiscoveredAuctionHouseRegistry.getInstance();
        registry.addOrUpdateOwnInfo();
        return registry.getDiscoveredAuctionHousesInclusiveOwn().toArray(new DiscoveredAuctionHouseInfo[0]);
    }
}

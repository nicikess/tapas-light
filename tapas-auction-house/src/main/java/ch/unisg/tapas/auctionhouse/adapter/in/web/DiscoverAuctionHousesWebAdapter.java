package ch.unisg.tapas.auctionhouse.adapter.in.web;

import ch.unisg.tapas.auctionhouse.application.port.in.DiscoverAuctionHousesCommand;
import ch.unisg.tapas.auctionhouse.application.port.in.DiscoverAuctionHousesUseCase;
import ch.unisg.tapas.auctionhouse.domain.DiscoveredAuctionHouseRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class DiscoverAuctionHousesWebAdapter {

    private final DiscoverAuctionHousesUseCase discoverAuctionHousesUseCase;

    @GetMapping(path = { "/discover", "/crawl" })
    public ResponseEntity<String> discoverAuctionHouses(
        @RequestParam(name = "url") String urlParam,
        @RequestParam(name = "taskTypes", required = false) String[] taskTypesParam
    ) {
        var taskTypes = taskTypesParam != null ? taskTypesParam : new String[0];

        discoverAuctionHousesUseCase.discoverAuctionHouses(
            new DiscoverAuctionHousesCommand(urlParam, taskTypes)
        );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Debug
    @GetMapping(path = "/discover/clear")
    public ResponseEntity<String> clearDiscoveredAuctionHouses() {
        DiscoveredAuctionHouseRegistry.getInstance().clear();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

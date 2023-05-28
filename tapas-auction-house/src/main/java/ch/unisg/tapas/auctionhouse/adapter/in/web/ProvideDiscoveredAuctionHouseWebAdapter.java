package ch.unisg.tapas.auctionhouse.adapter.in.web;

import ch.unisg.tapas.auctionhouse.adapter.common.formats.DiscoveredAuctionHousesRepresentation;
import ch.unisg.tapas.auctionhouse.application.port.in.ProvideDiscoveredAuctionHousesCommand;
import ch.unisg.tapas.auctionhouse.application.port.in.ProvideDiscoveredAuctionHousesUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ProvideDiscoveredAuctionHouseWebAdapter {

    private final ProvideDiscoveredAuctionHousesUseCase provideDiscoveredAuctionHousesUseCase;

    @GetMapping(path = { "/discovery", "/discovery/" } )
    public ResponseEntity<String> handleDiscover() {

        var discoveredAuctionHouses = provideDiscoveredAuctionHousesUseCase.provideDiscoveredAuctionHouses(
            new ProvideDiscoveredAuctionHousesCommand()
        );

        try {
            var json = DiscoveredAuctionHousesRepresentation.serialize(discoveredAuctionHouses);
            var headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, DiscoveredAuctionHousesRepresentation.MEDIA_TYPE);
            return new ResponseEntity<>(json, headers, HttpStatus.ACCEPTED);
        } catch (JsonProcessingException ignored) { }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

package ch.unisg.tapas.auctionhouse.adapter.in.messaging.http;

import ch.unisg.tapas.auctionhouse.adapter.common.formats.AuctionJsonRepresentation;
import ch.unisg.tapas.auctionhouse.application.port.in.AuctionStartedEvent;
import ch.unisg.tapas.auctionhouse.application.port.in.AuctionStartedEventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class AuctionStartedEventListenerHttpAdapter {

    private final AuctionStartedEventHandler auctionStartedEventHandler;

    @PostMapping(path = "/auction",  consumes = {AuctionJsonRepresentation.MEDIA_TYPE})
    public ResponseEntity<String> handleAuctionStartedEvent(@RequestBody AuctionJsonRepresentation auctionRepresentation) {
        var event = new AuctionStartedEvent(auctionRepresentation.deserialize());
        var ok = auctionStartedEventHandler.handleAuctionStartedEvent(event);
        return new ResponseEntity<>(ok ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}

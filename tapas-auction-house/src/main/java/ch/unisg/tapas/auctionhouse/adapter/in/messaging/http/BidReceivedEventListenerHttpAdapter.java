package ch.unisg.tapas.auctionhouse.adapter.in.messaging.http;

import ch.unisg.tapas.auctionhouse.adapter.common.formats.BidJsonRepresentation;
import ch.unisg.tapas.auctionhouse.application.handler.BidReceivedHandler;
import ch.unisg.tapas.auctionhouse.application.port.in.BidReceivedEvent;
import ch.unisg.tapas.auctionhouse.application.port.in.BidReceivedEventHandler;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class BidReceivedEventListenerHttpAdapter {

    private final BidReceivedEventHandler bidReceivedEventHandler;

    @PostMapping(path = { "/bid", "/bid/" },  consumes = {BidJsonRepresentation.MEDIA_TYPE})
    public ResponseEntity<String> handleBidReceivedEvent(
        @RequestBody BidJsonRepresentation bidRepresentation)
    {
        var event = new BidReceivedEvent(bidRepresentation);
        var bidStatus = bidReceivedEventHandler.handleBidReceivedEvent(event);

        HttpStatus responseStatusCode;

        switch (bidStatus) {
            case OK:
                responseStatusCode = HttpStatus.NO_CONTENT;
                break;
            case TOO_LATE:
                responseStatusCode = HttpStatus.GONE;
                break;
            case NO_AUCTION:
            default:
                responseStatusCode = HttpStatus.NOT_FOUND;
                break;
        }

        return new ResponseEntity<>(responseStatusCode);
    }
}

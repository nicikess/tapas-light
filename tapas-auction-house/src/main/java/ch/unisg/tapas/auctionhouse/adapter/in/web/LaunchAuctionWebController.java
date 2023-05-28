package ch.unisg.tapas.auctionhouse.adapter.in.web;

import ch.unisg.tapas.auctionhouse.adapter.common.formats.AuctionJsonRepresentation;
import ch.unisg.tapas.auctionhouse.application.port.in.LaunchAuctionCommand;
import ch.unisg.tapas.auctionhouse.application.port.in.LaunchAuctionUseCase;
import ch.unisg.tapas.auctionhouse.domain.Auction;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import java.net.URI;
import java.sql.Timestamp;

/**
 * Controller that handles HTTP requests for launching auctions. This controller implements the
 * {@link LaunchAuctionUseCase} use case using the {@link LaunchAuctionCommand}.
 */
@RestController
public class LaunchAuctionWebController {
    private final LaunchAuctionUseCase launchAuctionUseCase;

    /**
     * Constructs the controller.
     *
     * @param launchAuctionUseCase an implementation of the launch auction use case
     */
    public LaunchAuctionWebController(LaunchAuctionUseCase launchAuctionUseCase) {
        this.launchAuctionUseCase = launchAuctionUseCase;
    }

    /**
     * Handles HTTP POST requests for launching auctions. Note: you are free to modify this handler
     * as you see fit to reflect the discussions for the uniform HTTP API for the auction house.
     * You should also ensure that this handler has the exact behavior you would expect from the
     * defined uniform HTTP API (status codes, returned payload, HTTP headers, etc.)
     *
     * @param payload a representation of the auction to be launched
     * @return
     */
    @PostMapping(path = "/auctions/", consumes = AuctionJsonRepresentation.MEDIA_TYPE)
    public ResponseEntity<String> launchAuction(@RequestBody AuctionJsonRepresentation payload) {
        Auction.AuctionDeadline deadline = (payload.getDeadline() == null) ?
            null : new Auction.AuctionDeadline(Timestamp.valueOf(payload.getDeadline()));

        LaunchAuctionCommand command = new LaunchAuctionCommand(
            new Auction.AuctionedTaskUri(URI.create(payload.getTaskUri())),
            new Auction.AuctionedTaskType(payload.getTaskType()),
            deadline
        );

        var auction = launchAuctionUseCase.launchAuction(command);

        try {
            var auctionJson = AuctionJsonRepresentation.serialize(auction);
            var responseHeaders = new HttpHeaders();
            responseHeaders.add(HttpHeaders.CONTENT_TYPE, AuctionJsonRepresentation.MEDIA_TYPE);
            return new ResponseEntity<>(auctionJson, responseHeaders, HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

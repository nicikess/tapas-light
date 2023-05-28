package ch.unisg.tapas.auctionhouse.adapter.common.formats;

import ch.unisg.tapas.auctionhouse.domain.Auction;
import ch.unisg.tapas.auctionhouse.domain.Bid;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.net.URI;

/**
 * Used to expose a representation of the state of an auction through an interface. This class is
 * only meant as a starting point when defining a uniform HTTP API for the Auction House: feel free
 * to modify this class as you see fit!
 */
public class BidJsonRepresentation {
    public static final String MEDIA_TYPE = "application/bid+json";

    @Getter @Setter
    private String auctionId;

    @Getter @Setter
    private String bidderName;

    @Getter @Setter
    private String bidderAuctionHouseUri;

    @Getter @Setter
    private String bidderTaskListUri;

    public BidJsonRepresentation() {}

    public BidJsonRepresentation(Bid bid) {
        this.auctionId = bid.getAuctionId().getValue();
        this.bidderName = bid.getBidderName().getValue();
        this.bidderAuctionHouseUri = bid.getBidderAuctionHouseUri().getValue().toString();
        this.bidderTaskListUri = bid.getBidderTaskListUri().getValue().toString();
    }

    public static String serialize(Bid bid) throws JsonProcessingException {
        BidJsonRepresentation representation = new BidJsonRepresentation(bid);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsString(representation);
    }

    public static Bid deserialize(BidJsonRepresentation representation) {
        return new Bid(
            new Auction.AuctionId(representation.auctionId),
            new Bid.BidderName(representation.bidderName),
            new Bid.BidderAuctionHouseUri(URI.create(representation.bidderAuctionHouseUri)),
            new Bid.BidderTaskListUri(URI.create(representation.bidderTaskListUri))
        );
    }

    public Bid deserialize() {
        return BidJsonRepresentation.deserialize(this);
    }
}

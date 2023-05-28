package ch.unisg.tapas.auctionhouse.application.port.in;

import ch.unisg.tapas.auctionhouse.adapter.common.formats.BidJsonRepresentation;
import ch.unisg.tapas.common.SelfValidating;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Value
public class BidReceivedEvent extends SelfValidating<AuctionStartedEvent> {

    @Getter
    @NotNull
    BidJsonRepresentation bidJsonRepresentation;

    public BidReceivedEvent(BidJsonRepresentation bid) {
        this.bidJsonRepresentation = bid;
        this.validateSelf();
    }
}

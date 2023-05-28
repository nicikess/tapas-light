package ch.unisg.tapas.auctionhouse.application.port.in;

import lombok.Value;

@Value
public class DiscoverAuctionHousesCommand {
    String url;
    String[] taskTypes;
}

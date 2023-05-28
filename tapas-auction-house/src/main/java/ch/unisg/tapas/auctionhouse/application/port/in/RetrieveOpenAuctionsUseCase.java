package ch.unisg.tapas.auctionhouse.application.port.in;

import ch.unisg.tapas.auctionhouse.domain.Auction;

import java.util.Collection;

public interface RetrieveOpenAuctionsUseCase {

    Collection<Auction> retrieveAuctions(RetrieveOpenAuctionsQuery query);
}

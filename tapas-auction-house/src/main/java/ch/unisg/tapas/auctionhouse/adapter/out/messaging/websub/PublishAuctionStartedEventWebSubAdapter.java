package ch.unisg.tapas.auctionhouse.adapter.out.messaging.websub;

import ch.unisg.tapas.auctionhouse.adapter.common.clients.WebSubPublisher;
import ch.unisg.tapas.auctionhouse.application.port.out.AuctionStartedEventPort;
import ch.unisg.tapas.auctionhouse.domain.AuctionStartedEvent;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PublishAuctionStartedEventWebSubAdapter implements AuctionStartedEventPort {

    private static final Logger LOGGER = LogManager.getLogger(PublishAuctionStartedEventWebSubAdapter.class);

    private final WebSubPublisher webSubPublisher;

    @Override
    public void publishAuctionStartedEvent(AuctionStartedEvent event) {
        LOGGER.info("Publish Auction Started Event via WebSub");
        webSubPublisher.notifyHub("/auctions/");
    }
}

package ch.unisg.tapas.auctionhouse.adapter.out.messaging;

import ch.unisg.tapas.auctionhouse.adapter.out.messaging.mqtt.PublishAuctionStartedEventMqttAdapter;
import ch.unisg.tapas.auctionhouse.adapter.out.messaging.websub.PublishAuctionStartedEventWebSubAdapter;
import ch.unisg.tapas.auctionhouse.application.port.out.AuctionStartedEventPort;
import ch.unisg.tapas.auctionhouse.domain.AuctionStartedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Primary
public class PublishAuctionStartedEventAllChannelAdapter implements AuctionStartedEventPort {

    private final PublishAuctionStartedEventMqttAdapter mqttAdapter;
    private final PublishAuctionStartedEventWebSubAdapter webSubAdapter;

    @Override
    public void publishAuctionStartedEvent(AuctionStartedEvent event) {
        mqttAdapter.publishAuctionStartedEvent(event);
        webSubAdapter.publishAuctionStartedEvent(event);
    }
}

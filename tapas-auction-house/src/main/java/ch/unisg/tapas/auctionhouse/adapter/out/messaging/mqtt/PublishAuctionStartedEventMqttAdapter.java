package ch.unisg.tapas.auctionhouse.adapter.out.messaging.mqtt;

import ch.unisg.tapas.auctionhouse.adapter.common.clients.TapasMqttClient;
import ch.unisg.tapas.auctionhouse.adapter.common.formats.AuctionJsonRepresentation;
import ch.unisg.tapas.auctionhouse.application.port.out.AuctionStartedEventPort;
import ch.unisg.tapas.auctionhouse.domain.AuctionStartedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Component;

@Component
public class PublishAuctionStartedEventMqttAdapter implements AuctionStartedEventPort {

    private static final Logger LOGGER = LogManager.getLogger(PublishAuctionStartedEventMqttAdapter.class);

    @Override
    public void publishAuctionStartedEvent(AuctionStartedEvent event) {
        LOGGER.info("Publish Auction Started Event via MQTT");
        try {
            var json = AuctionJsonRepresentation.serialize(event.getAuction());
            TapasMqttClient.getInstance(null ,null)
                .publishMessage("ch/unisg/tapas/auctions", json);
        } catch (MqttException | JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}

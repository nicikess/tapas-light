package ch.unisg.tapas.auctionhouse.adapter.in.messaging.mqtt;

import ch.unisg.tapas.auctionhouse.adapter.common.formats.AuctionJsonRepresentation;
import ch.unisg.tapas.auctionhouse.application.port.in.AuctionStartedEvent;
import ch.unisg.tapas.auctionhouse.application.port.in.AuctionStartedEventHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctionStartedEventMqttListener extends AuctionEventMqttListener {

    private static final Logger LOGGER = LogManager.getLogger(AuctionStartedEventMqttListener.class);

    private final AuctionStartedEventHandler auctionStartedEventHandler;

    @Override
    public boolean handleEvent(MqttMessage message) {
        LOGGER.info("Handle Auction Started Event from MQTT");
        var payloadAuctionEvent = new String(message.getPayload());
        try {
            LOGGER.debug("Received new open auction from AuctionStartedEvent via MQTT");
            var auction = AuctionJsonRepresentation.fromJsonString(payloadAuctionEvent).deserialize();
            var auctionStartedEvent = new AuctionStartedEvent(auction);
            auctionStartedEventHandler.handleAuctionStartedEvent(auctionStartedEvent);
        } catch (JsonProcessingException e) {
            LOGGER.warn("MQTT message error for AuctionStartedEvent");
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

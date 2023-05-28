package ch.unisg.tapas.auctionhouse.adapter.in.messaging.mqtt;

import ch.unisg.tapas.auctionhouse.application.port.in.AuctionStartedEventHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Component;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * Dispatches MQTT messages for known topics to associated event listeners. Used in conjunction with
 * {@link ch.unisg.tapas.auctionhouse.adapter.common.clients.TapasMqttClient}.
 *
 * This is where you would define MQTT topics and map them to event listeners (see
 * {@link AuctionEventsMqttDispatcher#initRouter()}).
 *
 * This class is only provided as an example to help you bootstrap the project. You are welcomed to
 * change this class as you see fit.
 */
@Component
public class AuctionEventsMqttDispatcher {

    private static final Logger LOGGER = LogManager.getLogger(AuctionEventsMqttDispatcher.class);

    private final Map<String, AuctionEventMqttListener> router = new Hashtable<>();
    private final AuctionStartedEventMqttListener auctionStartedEventMqttListener;

    public AuctionEventsMqttDispatcher(AuctionStartedEventMqttListener auctionStartedEventMqttListener) {
        this.auctionStartedEventMqttListener = auctionStartedEventMqttListener;
        initRouter();
    }

    private void initRouter() {
        router.put("ch/unisg/tapas/auctions", auctionStartedEventMqttListener);
    }

    /**
     * Returns all topics registered with this dispatcher.
     *
     * @return the set of registered topics
     */
    public Set<String> getAllTopics() {
        return router.keySet();
    }

    /**
     * Dispatches an event received via MQTT for a given topic.
     *
     * @param topic the topic for which the MQTT message was received
     * @param message the received MQTT message
     */
    public void dispatchEvent(String topic, MqttMessage message) {
        LOGGER.info("Dispatching MQTT Message");
        AuctionEventMqttListener listener = router.get(topic);
        listener.handleEvent(message);
    }
}

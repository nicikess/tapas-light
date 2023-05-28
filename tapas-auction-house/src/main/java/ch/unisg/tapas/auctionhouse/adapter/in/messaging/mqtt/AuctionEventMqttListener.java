package ch.unisg.tapas.auctionhouse.adapter.in.messaging.mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Abstract MQTT listener for auction-related events
 */
public abstract class AuctionEventMqttListener {

    public abstract boolean handleEvent(MqttMessage message);
}

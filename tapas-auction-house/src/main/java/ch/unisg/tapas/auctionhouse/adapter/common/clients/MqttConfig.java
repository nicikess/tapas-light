package ch.unisg.tapas.auctionhouse.adapter.common.clients;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MqttConfig {

    @Getter
    @Value("${mqtt.broker}")
    private String broker;

    @Getter
    @Value("${mqtt.bootstrap}")
    private boolean bootstrap;
}

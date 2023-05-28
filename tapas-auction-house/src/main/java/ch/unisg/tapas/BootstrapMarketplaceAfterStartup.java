package ch.unisg.tapas;

import ch.unisg.tapas.auctionhouse.adapter.common.clients.*;
import ch.unisg.tapas.auctionhouse.adapter.in.messaging.mqtt.AuctionEventsMqttDispatcher;
import ch.unisg.tapas.common.AuctionHouseResourceDirectory;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;

@Profile("!test")
@AllArgsConstructor
@Component
public class BootstrapMarketplaceAfterStartup {

    private static final Logger LOGGER = LogManager.getLogger(BootstrapMarketplaceAfterStartup.class);

    public static final String RESOURCE_DIRECTORY = "https://api.interactions.ics.unisg.ch/auction-houses/";

    private final MqttConfig mqttConfig;
    private final AuctionEventsMqttDispatcher mqttDispatcher;
    private final WebSubConfig webSubConfig;
    private final ResourceDirectoryRegisterer resourceDirectoryRegisterer;

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        resourceDirectoryRegisterer.unregister();
        resourceDirectoryRegisterer.register();

        if (mqttConfig.isBootstrap()) {
            bootstrapMarketplaceWithMqtt();
        } else {
            LOGGER.info("Bootstrapping marketplaces with MQTT is skipped");
        }

        if (webSubConfig.isBootstrap()) {
            if (webSubConfig.isProductionEnvironment()) {
                bootstrapMarketplaceWithWebSub();
            } else {
                LOGGER.info("Subscribing to development WebSubHub");
                var subscriber = new WebSubSubscriber(webSubConfig);
                subscriber.subscribeToDevelopmentWebSubHub();
            }
        } else {
            LOGGER.info("Bootstrapping marketplaces with WebSub is skipped");
        }
    }

    /**
     * Discovers auction houses and subscribes to WebSub notifications
     */
    private void bootstrapMarketplaceWithWebSub() {
        LOGGER.info("Start bootstrapping marketplaces with WebSub");
        List<String> auctionHouseEndpoints = discoverAuctionHouseEndpoints();
        LOGGER.info("Found auction house endpoints: " + auctionHouseEndpoints);
        var subscriber = new WebSubSubscriber(webSubConfig);
        for (String endpoint : auctionHouseEndpoints) {
            subscriber.subscribeToAuctionHouseEndpoint(URI.create(endpoint));
        }
        LOGGER.info("Finished bootstrapping marketplaces with WebSub");
    }

    /**
     * Connects to an MQTT broker, presumably the one used by all TAPAS groups to communicate with
     * one another
     */
    private void bootstrapMarketplaceWithMqtt() {
        LOGGER.info("Start bootstrapping marketplaces with MQTT");
        try {
            var client = TapasMqttClient.getInstance(mqttConfig.getBroker(), mqttDispatcher);
            client.startReceivingMessages();
        } catch (MqttException e) {
            LOGGER.error(e.getMessage(), e);
        }
        LOGGER.info("Finished bootstrapping marketplaces with MQTT");
    }

    private List<String> discoverAuctionHouseEndpoints() {
        LOGGER.info("Discovering Auction House Endpoints");
        var directory = new AuctionHouseResourceDirectory(
            URI.create(RESOURCE_DIRECTORY)
        );
        return directory.retrieveAuctionHouseEndpoints();
    }
}

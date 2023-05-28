package ch.unisg.tapas.auctionhouse.application.service;

import ch.unisg.tapas.auctionhouse.adapter.common.clients.WebSubConfig;
import ch.unisg.tapas.auctionhouse.adapter.common.clients.WebSubSubscriber;
import ch.unisg.tapas.auctionhouse.adapter.common.formats.DiscoveredAuctionHousesRepresentation;
import ch.unisg.tapas.auctionhouse.application.port.in.DiscoverAuctionHousesCommand;
import ch.unisg.tapas.auctionhouse.application.port.in.DiscoverAuctionHousesUseCase;
import ch.unisg.tapas.auctionhouse.domain.DiscoveredAuctionHouseInfo;
import ch.unisg.tapas.auctionhouse.domain.DiscoveredAuctionHouseRegistry;
import ch.unisg.tapascommon.configuration.ServiceConfiguration;
import ch.unisg.tapascommon.communication.WebClient;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.HashSet;

@RequiredArgsConstructor
@Component
public class DiscoverAuctionHousesService implements DiscoverAuctionHousesUseCase {

    private static final Logger LOGGER = LogManager.getLogger(DiscoverAuctionHousesService.class);

    private static final DiscoveredAuctionHouseRegistry registry = DiscoveredAuctionHouseRegistry.getInstance();

    private final WebSubConfig webSubConfig;

    private static final String DISCOVERY_PATH = "/discovery/";

    @Override
    public void discoverAuctionHouses(DiscoverAuctionHousesCommand command) {
        LOGGER.info("Start discovering Auction Houses");
        registry.clear();
        var crawledAuctionHouseUrls = new HashSet<String>();
        crawledAuctionHouseUrls.add(ServiceConfiguration.LOCALHOST_AUCTION_HOUSE);
        crawledAuctionHouseUrls.add(ServiceConfiguration.PUBLIC_AUCTION_HOUSE);
        queryAuctionHouse(crawledAuctionHouseUrls, command.getUrl(), command.getTaskTypes());
        LOGGER.info("Finished discovering Auction Houses");
        registerToWebSubHubs();
    }

    private void queryAuctionHouse(HashSet<String> crawledAuctionHouseUrls, String url, String[] taskTypes) {

        LOGGER.info("Crawling Auction House with URL: " + url);

        if (crawledAuctionHouseUrls.contains(url)) {
            LOGGER.info("Skipping already crawled Auction House");
            return;
        }

        try {
            crawledAuctionHouseUrls.add(url);

            var discoveryUrl = WebClient.normalizeUrl(url + DISCOVERY_PATH);
            var response = WebClient.get(discoveryUrl);

            if (!WebClient.checkResponseStatusCode(response)) {
                LOGGER.warn("Failed to query discovery Auction House with URL: " + url);
                return;
            }

            var json = response.body();
            var infos =  DiscoveredAuctionHousesRepresentation.deserialize(json);

            addAuctionHousesToRegistry(infos, taskTypes);

            for (var info : infos) {
                queryAuctionHouse(crawledAuctionHouseUrls, info.getAuctionHouseUri(), taskTypes);
            }

        } catch (Exception ignored) {
            LOGGER.warn("Failed to discover Action Houses from Auction House with URL: " + url);
        }
    }

    private void addAuctionHousesToRegistry(DiscoveredAuctionHouseInfo[] infos, String[] taskTypes) {
        if (taskTypes.length > 0) {
            for (var info : infos) {
                if (info.getGroupName().equals(ServiceConfiguration.GROUP_NAME)) {
                    continue;
                }

                for (var infoType : info.getTaskTypes()) {
                    for (var type : taskTypes) {
                        if (infoType.equalsIgnoreCase(type)) {
                            registry.addDiscoveredAuctionHouse(info);
                        }
                    }
                }
            }
        } else {
            registry.addAllDiscoveredAuctionHouses(infos);
        }
    }

    private void registerToWebSubHubs() {
        var auctionHouseEndpoints = registry.getDiscoveredAuctionHouses();
        LOGGER.info("Found auction house endpoints: " + auctionHouseEndpoints.size());
        var subscriber = new WebSubSubscriber(webSubConfig);
        for (var endpoints : auctionHouseEndpoints) {
            subscriber.subscribeToAuctionHouseEndpoint(URI.create(endpoints.getWebSubUri()));
        }
    }
}

package ch.unisg.tapas.auctionhouse.adapter.common.clients;

import ch.unisg.tapascommon.communication.WebClient;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/**
 * Subscribes to the WebSub hubs of auction houses discovered at run time. This class is instantiated
 * from {@link ch.unisg.tapas.TapasAuctionHouseApplication} when bootstrapping the TAPAS marketplace
 * via WebSub.
 */
@RequiredArgsConstructor
public class WebSubSubscriber {

    private static final Logger LOGGER = LogManager.getLogger(WebSubSubscriber.class);

    private static final String CALLBACK_PATH = "/subscribe";
    private static final String SUBSCRIPTION_PARAMETERS = "hub.callback=<HUB_CALLBACK>&hub.mode=subscribe&hub.topic=<HUB_TOPIC>";
    private static final String CONTENT_TYPE_PRODUCTION = "application/x-www-form-urlencoded";
    private static final String CONTENT_TYPE_DEVELOPMENT = "application/json";

    private static String buildSubscriptionString(String hubCallback, String hubTopic) {
        return SUBSCRIPTION_PARAMETERS
            .replace("<HUB_CALLBACK>", URLEncoder.encode(hubCallback, StandardCharsets.UTF_8))
            .replace("<HUB_TOPIC>", URLEncoder.encode(hubTopic, StandardCharsets.UTF_8));
    }

    private final WebSubConfig webSubConfig;

    private String hub = "";
    private String topic = "";

    private boolean checkUri(String uri) {
        try {
            var u = new URL(uri);
            u.toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException ignored) { }
        return false;
    }

    private boolean discoverWebSubHub(String auctionHouseUri) {
        if (!checkUri(auctionHouseUri)) {
            return false;
        }

        HttpResponse<String> response;
        try {
            response = WebClient.get(auctionHouseUri);
        } catch (IOException | InterruptedException e) {
            LOGGER.info("Failed to discover WebSubHub from Auction House with URI " + auctionHouseUri);
            return false;
        }

        var linkHeaders = response.headers().allValues("Link");
        for (var linkHeader : linkHeaders) {
            var split = linkHeader.split(",");
            for (var link : split) {
                if (link.contains("rel=\"hub\"")) {
                    var start = link.indexOf('<') + 1;
                    var end = link.indexOf('>');
                    hub = WebClient.normalizeUrl(link.substring(start, end));
                } else if (link.contains("rel=\"self\"")) {
                    var start = link.indexOf('<') + 1;
                    var end = link.indexOf('>');
                    topic = WebClient.normalizeUrl(link.substring(start, end));
                }
            }
        }

        if (hub.isEmpty() || topic.isEmpty()) {
            LOGGER.info("Failed to discover WebSubHub from Auction House with URI " + auctionHouseUri);
            return false;
        }

        LOGGER.info("Successfully discovered WebSubHub for Auction House with URI " + auctionHouseUri);
        return true;
    }

    private void subscribeToWebSubHub() {
        HttpResponse<String> response = null;

        if (webSubConfig.isProductionEnvironment()) {
            var parameters = buildSubscriptionString(webSubConfig.getSelf() + CALLBACK_PATH, topic);
            try {
                response = WebClient.post(hub, parameters, CONTENT_TYPE_PRODUCTION);
            } catch (IOException | InterruptedException ignored) { }
        } else {
            var json = new JSONObject()
                .put("hub.callback", webSubConfig.getSelf() + CALLBACK_PATH)
                .put("hub.mode", "subscribe")
                .put("hub.topic", topic)
                .put("hub.ws", false)
                .toString();

            try {
                response = WebClient.post(hub, json, CONTENT_TYPE_DEVELOPMENT);
            } catch (IOException | InterruptedException ignored) { }
        }

        if (WebClient.checkResponseStatusCode(response)) {
            LOGGER.info("Successfully subscribed to WebSubHub " + hub + " with Topic " + topic);
        } else {
            LOGGER.warn("Failed to subscribe to WebSubHub" + hub + " with Topic " + topic);
        }
    }

    public void subscribeToAuctionHouseEndpoint(URI endpoint) {
        if (discoverWebSubHub(endpoint.toString())) {
            subscribeToWebSubHub();
        }
    }

    public void subscribeToDevelopmentWebSubHub() {
        hub = webSubConfig.getHub();
        topic = webSubConfig.getSelfTopic();
        subscribeToWebSubHub();
    }
}

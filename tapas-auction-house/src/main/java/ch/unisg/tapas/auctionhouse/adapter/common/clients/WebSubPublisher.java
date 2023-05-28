package ch.unisg.tapas.auctionhouse.adapter.common.clients;

import ch.unisg.tapascommon.communication.WebClient;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Component
public class WebSubPublisher {

    private static final Logger LOGGER = LogManager.getLogger(WebSubPublisher.class);

    private static final String PUBLISH_PARAMETERS = "hub.url=<HUB_URL>&hub.mode=publish";
    private static final String CONTENT_TYPE_PRODUCTION = "application/x-www-form-urlencoded";
    private static final String CONTENT_TYPE_DEVELOPMENT = "application/json";

    private final WebSubConfig webSubConfig;

    private String buildPublishString(String path) {
        return PUBLISH_PARAMETERS
            .replace("<HUB_URL>", URLEncoder.encode(webSubConfig.getSelf() + path, StandardCharsets.UTF_8));
    }

    @Async
    public void notifyHub(String path) {
        HttpResponse<String> response = null;

        if (webSubConfig.isProductionEnvironment()) {
            var parameters = buildPublishString(path);
            try {
                response = WebClient.post(webSubConfig.getHubPublish(), parameters, CONTENT_TYPE_PRODUCTION);
            } catch (IOException | InterruptedException ignored) { }
        } else {
            var json = new JSONObject()
                .put("hub.mode", "publish")
                .put("hub.url", webSubConfig.getSelf() + path)
                .toString();

            try {
                response = WebClient.post(webSubConfig.getHubPublish() + "/publish", json, CONTENT_TYPE_DEVELOPMENT);
            } catch (IOException | InterruptedException ignored) { }
        }

        var ok = WebClient.checkResponseStatusCode(response);

        if (ok) {
            LOGGER.info("Successfully notified WebSubHub");
        } else {
            LOGGER.warn("Failed to notify WebSubHub");
        }

    }
}

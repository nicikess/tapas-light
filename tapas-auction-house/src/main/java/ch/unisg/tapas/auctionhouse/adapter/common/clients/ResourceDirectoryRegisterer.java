package ch.unisg.tapas.auctionhouse.adapter.common.clients;

import ch.unisg.tapascommon.configuration.ServiceConfiguration;
import ch.unisg.tapascommon.communication.WebClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class ResourceDirectoryRegisterer {

    private static final Logger LOGGER = LogManager.getLogger(ResourceDirectoryRegisterer.class);

    private static final String RESOURCE_DIRECTORY = "https://api.interactions.ics.unisg.ch/auction-houses/";

    private final WebSubConfig webSubConfig;

    public boolean register() {
        var json = new JSONObject()
            .put("group", ServiceConfiguration.GROUP_NAME)
            .put("endpoint", webSubConfig.getSelf())
            .toString();

        try {
            var response = WebClient.post(RESOURCE_DIRECTORY, json, "application/json");
            var ok = WebClient.checkResponseStatusCode(response);
            if (ok) {
                LOGGER.info("Registered to resource directory");
            } else {
                LOGGER.warn("Failed to register to resource directory");
            }
            return ok;
        } catch (IOException | InterruptedException ignored) { }
        return false;
    }

    public boolean unregister() {
        try {
            var response = WebClient.get(RESOURCE_DIRECTORY);
            var payload = new ObjectMapper().readTree(response.body());
            for (JsonNode node : payload) {
                if (node.get("group").textValue().equals(ServiceConfiguration.GROUP_NAME)) {
                    var responseDelete = WebClient.delete(
                        RESOURCE_DIRECTORY + node.get("id").textValue(), null, null);
                    var ok = WebClient.checkResponseStatusCode(responseDelete);

                    if (ok) {
                        LOGGER.info("Unregistered from resource directory");
                    } else {
                        LOGGER.warn("Failed to unregister from resource directory");
                    }
                    return ok;
                }
            }
        } catch (IOException | InterruptedException ignored) { }
        return false;
    }
}

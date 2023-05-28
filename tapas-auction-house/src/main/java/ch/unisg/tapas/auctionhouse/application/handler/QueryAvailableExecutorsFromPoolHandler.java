package ch.unisg.tapas.auctionhouse.application.handler;

import ch.unisg.tapascommon.configuration.ServiceConfiguration;
import ch.unisg.tapascommon.communication.WebClient;
import ch.unisg.tapascommon.pool.adapter.common.formats.ExecutorJsonRepresentation;
import ch.unisg.tapas.auctionhouse.application.port.out.QueryAvailableExecutorsFromPoolEvent;
import ch.unisg.tapas.auctionhouse.application.port.out.QueryAvailableExecutorsFromPoolEventHandler;
import ch.unisg.tapas.auctionhouse.domain.ExecutorRegistry;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class QueryAvailableExecutorsFromPoolHandler implements QueryAvailableExecutorsFromPoolEventHandler {

    private static final Logger LOGGER = LogManager.getLogger(QueryAvailableExecutorsFromPoolHandler.class);

    @Override
    public boolean retrieveAvailableExecutors(QueryAvailableExecutorsFromPoolEvent event) {
        LOGGER.info("Query Executors from Pool Service");
        var registry = ExecutorRegistry.getInstance();

        var json = "";
        try {
            json = queryPoolService();
        } catch (IOException | InterruptedException e) {
            LOGGER.warn("Failed to query available Executors from Pool");
            return false;
        }

        registry.clearExecutors();
        if (!json.isEmpty()) {
            var jsonArray = new JSONArray(json);
            for (var element : jsonArray) {
                try {
                    registry.addExecutor(ExecutorJsonRepresentation.fromJsonString(element.toString()).deserialize());
                } catch (JsonProcessingException e) {
                    LOGGER.warn("Failed to parse Executor JSON from Pool Query");
                }
            }
        }

        return true;
    }

    private String queryPoolService() throws IOException, InterruptedException {
        var response = WebClient.get(ServiceConfiguration.getExecutorPoolAddress() + "/available-executors/");
        if (WebClient.checkResponseStatusCode(response)) {
            return response.body();
        } else {
            throw new IOException();
        }
    }
}

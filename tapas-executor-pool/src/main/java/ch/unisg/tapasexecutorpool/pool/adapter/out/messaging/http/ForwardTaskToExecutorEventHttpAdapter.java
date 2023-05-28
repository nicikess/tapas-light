package ch.unisg.tapasexecutorpool.pool.adapter.out.messaging.http;

import ch.unisg.tapascommon.tasks.adapter.common.formats.TaskJsonRepresentation;
import ch.unisg.tapasexecutorpool.pool.application.port.out.ForwardTaskToExecutorEventPort;
import ch.unisg.tapasexecutorpool.pool.application.port.out.ForwardTaskToExecutorEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@Primary
public class ForwardTaskToExecutorEventHttpAdapter implements ForwardTaskToExecutorEventPort {

    private static final Logger LOGGER = LogManager.getLogger(ForwardTaskToExecutorEventHttpAdapter.class);

    @Override
    public void forwardTaskToExecutorEvent(ForwardTaskToExecutorEvent event) {
        try {
            var uri = event.getExecutor().getExecutorAddress().getValue() + "/execute-task/";
            var payload = TaskJsonRepresentation.serialize(event.getTask());
            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder().uri(URI.create(uri))
                    .setHeader(HttpHeaders.CONTENT_TYPE, TaskJsonRepresentation.MEDIA_TYPE)
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                LOGGER.info("Successfully forwarded Task to Executor: " + response);
            } else {
                LOGGER.info("Failed to forward Task to Executor: " + response);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

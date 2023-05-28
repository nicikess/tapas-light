package ch.unisg.tapasexecutorbase.executor.adapter.out.web;

import ch.unisg.tapascommon.communication.WebClient;
import ch.unisg.tapascommon.tasks.adapter.common.formats.TaskJsonPatchRepresentation;
import ch.unisg.tapasexecutorbase.executor.application.port.out.TaskUpdatedEventPort;
import ch.unisg.tapasexecutorbase.executor.domain.TaskUpdatedEvent;
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

@Primary
@Component
public class PublishTaskUpdatedEventWebAdapter implements TaskUpdatedEventPort {

    private static final Logger LOGGER = LogManager.getLogger(PublishTaskUpdatedEventWebAdapter.class);

    @Override
    public void updateTaskStatusEvent(TaskUpdatedEvent event) {
        try {
            var uri = event.getTaskUri();
            var json = TaskJsonPatchRepresentation.getPatchRepresentation(event.getTaskStatus(), event.getOutputData());
            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .setHeader(HttpHeaders.CONTENT_TYPE, TaskJsonPatchRepresentation.MEDIA_TYPE)
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(json))
                    .build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (WebClient.checkResponseStatusCode(response)) {
                LOGGER.info("Successfully sent update task status event");
            } else {
                LOGGER.warn("Failed to sent update task status event");
            }
        } catch (IOException | InterruptedException ignored) {
            LOGGER.warn("Failed to update task status");
        }
    }
}
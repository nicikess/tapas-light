package ch.unisg.tapasexecutorpool.pool.adapter.out.messaging.http;

import ch.unisg.tapascommon.tasks.adapter.common.formats.TaskJsonPatchRepresentation;
import ch.unisg.tapasexecutorpool.pool.application.port.out.TaskAssignedEvent;
import ch.unisg.tapasexecutorpool.pool.application.port.out.TaskAssignedEventPort;
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
public class PublishTaskAssignedEventHttpAdapter implements TaskAssignedEventPort {

    private static final Logger LOGGER = LogManager.getLogger(PublishTaskAssignedEventHttpAdapter.class);

    @Override
    public void handleTaskAssignedEvent(TaskAssignedEvent event) {
        LOGGER.info("Set Task Status to ASSIGNED");
        try {
            var uri = event.getTaskUri();
            var json = TaskJsonPatchRepresentation.getPatchRepresentationTaskAssigned(event.getServiceProvider());
            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .setHeader(HttpHeaders.CONTENT_TYPE, TaskJsonPatchRepresentation.MEDIA_TYPE)
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(json))
                    .build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
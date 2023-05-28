package ch.unisg.tapasroster.roster.adapter.out.messaging.http;

import ch.unisg.tapascommon.configuration.ServiceConfiguration;
import ch.unisg.tapascommon.tasks.adapter.common.formats.TaskJsonRepresentation;
import ch.unisg.tapasroster.roster.application.port.out.ForwardTaskToExecutorPoolEventPort;
import ch.unisg.tapasroster.roster.application.port.out.ForwardTaskToExecutorPoolEvent;
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
public class ForwardTaskToExecutorPoolEventHttpAdapter implements ForwardTaskToExecutorPoolEventPort {

    private static final String URL = ServiceConfiguration.getExecutorPoolAddress();
    private static final String PATH = "/execute-task/";

    @Override
    public void forwardTaskToExecutorPoolEvent(ForwardTaskToExecutorPoolEvent event) {
        try {
            var payload = TaskJsonRepresentation.serialize(event.getTask());
            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(URL + PATH))
                    .setHeader(HttpHeaders.CONTENT_TYPE, TaskJsonRepresentation.MEDIA_TYPE)
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

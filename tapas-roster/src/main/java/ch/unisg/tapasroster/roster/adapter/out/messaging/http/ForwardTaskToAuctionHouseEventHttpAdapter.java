package ch.unisg.tapasroster.roster.adapter.out.messaging.http;

import ch.unisg.tapascommon.configuration.ServiceConfiguration;
import ch.unisg.tapasroster.roster.adapter.out.formats.NewAuctionJsonRepresentation;
import ch.unisg.tapasroster.roster.application.port.out.ForwardTaskToAuctionHouseEventPort;
import ch.unisg.tapasroster.roster.application.port.out.ForwardTaskToAuctionHouseEvent;
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
public class ForwardTaskToAuctionHouseEventHttpAdapter implements ForwardTaskToAuctionHouseEventPort {

    private static final String URL = ServiceConfiguration.getAuctionHouseAddress();
    private static final String PATH = "/auctions/";

    @Override
    public void forwardTaskToAuctionHouseEvent(ForwardTaskToAuctionHouseEvent event) {
        var json = NewAuctionJsonRepresentation.serialize(event.getTask(), event.getDeadline());
        try {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(URL + PATH))
                    .setHeader(HttpHeaders.CONTENT_TYPE, NewAuctionJsonRepresentation.MEDIA_TYPE)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

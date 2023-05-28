package ch.unisg.tapas.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that wraps up the resource directory used to discover auction houses in Week 6.
 */
public class AuctionHouseResourceDirectory {
    private final URI rdEndpoint;

    /**
     * Constructs a resource directory for auction house given a known URI.
     *
     * @param rdEndpoint the based endpoint of the resource directory
     */
    public AuctionHouseResourceDirectory(URI rdEndpoint) {
        this.rdEndpoint = rdEndpoint;
    }

    /**
     * Retrieves the endpoints of all auctions houses registered with this directory.
     * @return
     */
    public List<String> retrieveAuctionHouseEndpoints() {
        List<String> auctionHouseEndpoints = new ArrayList<>();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(rdEndpoint).GET().build();

            HttpResponse<String> response = HttpClient.newBuilder().build()
                .send(request, HttpResponse.BodyHandlers.ofString());

            // For simplicity, here we just hard code the current representation used by our
            // resource directory for auction houses
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode payload = objectMapper.readTree(response.body());

            for (JsonNode node : payload) {
                auctionHouseEndpoints.add(node.get("endpoint").asText());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return auctionHouseEndpoints;
    }
}

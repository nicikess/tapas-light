package ch.unisg.tapas.auctionhouse.adapter.in.web;

import ch.unisg.tapas.auctionhouse.adapter.common.formats.AuctionJsonRepresentation;
import ch.unisg.tapas.auctionhouse.application.port.in.RetrieveOpenAuctionsQuery;
import ch.unisg.tapas.auctionhouse.application.port.in.RetrieveOpenAuctionsUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.text.SimpleDateFormat;
import java.util.Date;

@RequiredArgsConstructor
@RestController
public class RetrieveOpenAuctionsFeedWebController {
    private final RetrieveOpenAuctionsUseCase retrieveAuctionListUseCase;

    @GetMapping(path = "/auctions/feed")
    public ResponseEntity<String> retrieveOpenAuctions() {
        var auctions = retrieveAuctionListUseCase.retrieveAuctions(new RetrieveOpenAuctionsQuery());

        var mapper = new ObjectMapper();
        var array = mapper.createArrayNode();

        for (var auction : auctions) {
            array.add(mapper.valueToTree(new AuctionJsonRepresentation(auction)));
        }

        var timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date());

        var json = new JSONObject()
            .put("version", "https://jsonfeed.org/version/1")
            .put("title", "Open Auctions Feed")
            .put("home_page_url", "")
            .put("feed_url", "")
            .put("updated", timeStamp)
            .put("items", array)
            .toString()
            .replace("\"[", "[")
            .replace("]\"", "]")
            .replace("\\\"", "\"");

        var responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        return new ResponseEntity<>(json, responseHeaders, HttpStatus.OK);
    }
}

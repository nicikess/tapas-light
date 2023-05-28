package ch.unisg.tapas.auctionhouse.adapter.common.clients;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class WebSubDiscover {

    private static final Logger LOGGER = LogManager.getLogger(WebSubDiscover.class);

    private final WebSubConfig webSubConfig;

    @GetMapping(path = "/")
    public ResponseEntity<String> handleDiscover() {
        LOGGER.info("Sending WebSub discover information");
        var headers = new HttpHeaders();
        headers.add("Link", "<" + webSubConfig.getHub() + ">; rel=\"hub\"");
        headers.add("Link", "<" + webSubConfig.getSelfTopic() + ">; rel=\"self\"");
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
}

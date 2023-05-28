package ch.unisg.tapascommon.communication;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class WebClientTest {

    @Test
    void normalizeUrlHttpsMalformed() {
        final var goodUrl = "https://tapas-auction-house.86-119-35-213.nip.io/auctions/";
        final var malformedUrl = "https://tapas-auction-house.86-119-35-213.nip.io//auctions/";
        final var normalized = WebClient.normalizeUrl(malformedUrl);
        assertThat(normalized).isEqualTo(goodUrl);
    }

    @Test
    void normalizeUrlHttpMalformed() {
        final var goodUrl = "http://tapas-auction-house.86-119-35-213.nip.io/auctions/";
        final var malformedUrl = "http://tapas-auction-house.86-119-35-213.nip.io//auctions/";
        final var normalized = WebClient.normalizeUrl(malformedUrl);
        assertThat(normalized).isEqualTo(goodUrl);
    }

    @Test
    void normalizeUrlHttpsGood() {
        final var goodUrl = "https://tapas-auction-house.86-119-35-213.nip.io/auctions/";
        final var malformedUrl = "https://tapas-auction-house.86-119-35-213.nip.io/auctions/";
        final var normalized = WebClient.normalizeUrl(malformedUrl);
        assertThat(normalized).isEqualTo(goodUrl);
    }
}

package ch.unisg.tapas.auctionhouse.adapter.common.formats;

import ch.unisg.tapas.auctionhouse.domain.DiscoveredAuctionHouseInfo;
import ch.unisg.tapascommon.tasks.domain.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DiscoveredAuctionHousesRepresentationTest {

    @Test
    void serialize() throws JsonProcessingException {
        final var infos = givenDiscoveredAuctionHouses();
        final var expectedJson = givenDiscoveredAuctionHousesJson();

        var json = DiscoveredAuctionHousesRepresentation.serialize(infos);

        assertThat(json).isEqualTo(expectedJson);
    }

    @Test
    void deserialize() throws JsonProcessingException {
        final var expectedInfos = givenDiscoveredAuctionHouses();
        final var json = givenDiscoveredAuctionHousesJson();

        var infos = DiscoveredAuctionHousesRepresentation.deserialize(json);

        assertThat(infos).isEqualTo(expectedInfos);
    }

    private DiscoveredAuctionHouseInfo[] givenDiscoveredAuctionHouses() {
        return new DiscoveredAuctionHouseInfo[] {
            new DiscoveredAuctionHouseInfo(
              "auctionHouseUri",
              "webSubUri",
              new String[] {Task.Type.BIGROBOT.name()},
              Timestamp.from(Instant.EPOCH),
              "test-group-1"
            ),
            new DiscoveredAuctionHouseInfo(
                "auctionHouseUri",
                "webSubUri",
                new String[] {Task.Type.COMPUTATION.name(), Task.Type.RANDOMTEXT.name()},
                Timestamp.from(Instant.EPOCH),
                "test-group-2"
            )
        };
    }

    private String givenDiscoveredAuctionHousesJson() {
        return "{\"auctionHouseInfo\":[{\"auctionHouseUri\":\"auctionHouseUri\",\"webSubUri\":\"webSubUri\",\"taskTypes\":[\"BIGROBOT\"],"
            + "\"timeStamp\":\"1970-01-01 00:00:00\",\"groupName\":\"test-group-1\"},{\"auctionHouseUri\":"
            + "\"auctionHouseUri\",\"webSubUri\":\"webSubUri\",\"taskTypes\":[\"COMPUTATION\",\"RANDOMTEXT\"],"
            + "\"timeStamp\":\"1970-01-01 00:00:00\",\"groupName\":\"test-group-2\"}]}";
    }
}

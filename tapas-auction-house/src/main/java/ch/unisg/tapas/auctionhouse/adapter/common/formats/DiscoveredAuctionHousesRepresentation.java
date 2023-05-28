package ch.unisg.tapas.auctionhouse.adapter.common.formats;

import ch.unisg.tapas.auctionhouse.domain.DiscoveredAuctionHouseInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscoveredAuctionHousesRepresentation {

    public final static String MEDIA_TYPE = "application/auctionhousediscovery+json";

    private DiscoveredAuctionHouseInfo[] auctionHouseInfo;

    public static DiscoveredAuctionHouseInfo[] deserialize(String json) throws JsonProcessingException {
        return new ObjectMapper().readValue(json, DiscoveredAuctionHousesRepresentation.class).auctionHouseInfo;
    }

    public static String serialize(DiscoveredAuctionHouseInfo[] discoveredAuctionHouses) throws JsonProcessingException {
        var representation = new DiscoveredAuctionHousesRepresentation(discoveredAuctionHouses);
        var mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        return mapper.writeValueAsString(representation);
    }
}

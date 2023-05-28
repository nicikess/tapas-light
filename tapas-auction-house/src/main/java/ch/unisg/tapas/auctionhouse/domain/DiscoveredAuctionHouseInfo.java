package ch.unisg.tapas.auctionhouse.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DiscoveredAuctionHouseInfo {

    private String auctionHouseUri;
    private String webSubUri;
    private String[] taskTypes;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp timeStamp;

    private String groupName;

    @Override
    public String toString() {
        return "DiscoveredAuctionHouseInfo{" +
            "auctionHouseUri='" + auctionHouseUri + '\'' +
            ", webSubUri='" + webSubUri + '\'' +
            ", taskTypes=" + Arrays.toString(taskTypes) +
            ", timeStamp=" + timeStamp +
            ", groupName='" + groupName + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscoveredAuctionHouseInfo that = (DiscoveredAuctionHouseInfo) o;
        return Objects.equals(groupName, that.groupName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupName);
    }
}

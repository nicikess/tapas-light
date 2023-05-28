package ch.unisg.tapas.auctionhouse.domain;

import ch.unisg.tapascommon.configuration.ServiceConfiguration;
import ch.unisg.tapascommon.tasks.domain.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DiscoveredAuctionHouseRegistry {

    private static final Logger LOGGER = LogManager.getLogger(DiscoveredAuctionHouseRegistry.class);

    private static final DiscoveredAuctionHouseRegistry instance = new DiscoveredAuctionHouseRegistry();

    private final Set<DiscoveredAuctionHouseInfo> auctionHouseInfoSet = new HashSet<>();

    public static DiscoveredAuctionHouseRegistry getInstance() {
        return instance;
    }

    private DiscoveredAuctionHouseRegistry() {
        addOrUpdateOwnInfo();
    }

    public void addOrUpdateOwnInfo() {
        var ownInfo = new DiscoveredAuctionHouseInfo();
        ownInfo.setAuctionHouseUri(ServiceConfiguration.getAuctionHouseAddress());

        var soughtTaskTypes = new ArrayList<String>();
        for(var type : Task.Type.values()) {
            if (type != Task.Type.UNKNOWN
                && type != Task.Type.BIGROBOT
                && type != Task.Type.COMPUTATION
                && type != Task.Type.TEMPERATURE) {
                soughtTaskTypes.add(type.name());
            }
        }

        ownInfo.setTaskTypes(soughtTaskTypes.toArray(new String[0]));
        ownInfo.setGroupName(ServiceConfiguration.GROUP_NAME);
        ownInfo.setWebSubUri(ServiceConfiguration.getAuctionHouseAddress());
        ownInfo.setTimeStamp(Timestamp.from(ZonedDateTime.now().toInstant()));

        auctionHouseInfoSet.remove(ownInfo);
        auctionHouseInfoSet.add(ownInfo);

        LOGGER.info("Updated own discovery info");
    }

    public void addDiscoveredAuctionHouse(DiscoveredAuctionHouseInfo info) {
        if (info.getGroupName().contains("" + ServiceConfiguration.GROUP_NUMBER)) {
            LOGGER.warn("Cannot add own info from foreign source");
            return;
        }

        boolean update = false;
        for (var entry : auctionHouseInfoSet) {
            if (entry.equals(info)) {
                if (entry.getTimeStamp().before(info.getTimeStamp())) {
                    update = true;
                }
            }
        }

        if (update) {
            auctionHouseInfoSet.remove(info);
            auctionHouseInfoSet.add(info);
            LOGGER.info("Updated previously discovered Auction House: " + info);
        } else {
            var added = auctionHouseInfoSet.add(info);
            if (added) {
                LOGGER.info("Added new discovered Auction House: " + info);
                LOGGER.info("Number of discovered Auction Houses: " + auctionHouseInfoSet.size());
            }
        }
    }

    public Set<DiscoveredAuctionHouseInfo> getDiscoveredAuctionHousesInclusiveOwn() {
        return auctionHouseInfoSet;
    }

    public Set<DiscoveredAuctionHouseInfo> getDiscoveredAuctionHouses() {
        var discovered = new HashSet<DiscoveredAuctionHouseInfo>();

        for (var info : auctionHouseInfoSet) {
            if (!info.getGroupName().equals(ServiceConfiguration.GROUP_NAME)) {
                discovered.add(info);
            }
        }

        return discovered;
    }

    public void clear() {
        auctionHouseInfoSet.clear();
    }

    public void addAllDiscoveredAuctionHouses(DiscoveredAuctionHouseInfo[] infos) {
        for (var info : infos) {
            addDiscoveredAuctionHouse(info);
        }
    }
}

package ch.unisg.tapas.auctionhouse.adapter.common.clients;

import ch.unisg.tapascommon.configuration.ServiceConfiguration;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WebSubConfig {
    @Value("${websub.environment}")
    private String environment;

    @Value("${websub.auction.house.uri.development}")
    private String ownAddressDevelopment;

    @Value("${websub.hub}")
    private String webSubHubAddress;

    @Value("${websub.hub.publish}")
    private String webSubHubPublishAddress;

    @Value("${websub.hub.development}")
    private String webSubHubAddressDevelopment;

    @Getter
    @Value("${websub.topic}")
    private String topic;

    @Getter
    @Value("${websub.bootstrap}")
    private boolean bootstrap;

    public boolean isProductionEnvironment() {
        return environment.equalsIgnoreCase("production");
    }

    public String getHub() {
        return isProductionEnvironment() ? webSubHubAddress : webSubHubAddressDevelopment;
    }

    public String getHubPublish() {
        return isProductionEnvironment() ? webSubHubPublishAddress : webSubHubAddressDevelopment;
    }

    public String getSelf() {
        return isProductionEnvironment() ? ServiceConfiguration.getAuctionHouseAddress() : ownAddressDevelopment;
    }

    public String getSelfTopic() {
        return getSelf() + topic;
    }
}

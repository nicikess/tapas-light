package ch.unisg.tapas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main TAPAS Auction House application.
 */
@SpringBootApplication
@EnableAsync
@ComponentScan(basePackages = { "ch.unisg.tapas", "ch.unisg.tapascommon" })
public class TapasAuctionHouseApplication {

    public static void main(String[] args) {
		SpringApplication tapasAuctioneerApp = new SpringApplication(TapasAuctionHouseApplication.class);
        tapasAuctioneerApp.run(args);
	}
}

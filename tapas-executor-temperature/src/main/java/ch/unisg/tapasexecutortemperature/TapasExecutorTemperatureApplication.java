package ch.unisg.tapasexecutortemperature;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "ch.unisg.tapasexecutortemperature", "ch.unisg.tapasexecutorbase", "ch.unisg.tapascommon" })
public class TapasExecutorTemperatureApplication {

	public static void main(String[] args) {
		SpringApplication.run(TapasExecutorTemperatureApplication.class, args);
	}
}

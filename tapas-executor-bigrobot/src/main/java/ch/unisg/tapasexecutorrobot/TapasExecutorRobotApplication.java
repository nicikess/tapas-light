package ch.unisg.tapasexecutorrobot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "ch.unisg.tapasexecutorrobot", "ch.unisg.tapasexecutorbase", "ch.unisg.tapascommon" })
public class TapasExecutorRobotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TapasExecutorRobotApplication.class, args);
	}
}

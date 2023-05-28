package ch.unisg.tapasexecutorcomputation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "ch.unisg.tapasexecutorcomputation", "ch.unisg.tapasexecutorbase", "ch.unisg.tapascommon" })
public class TapasExecutorComputationApplication {

	public static void main(String[] args) {
		SpringApplication.run(TapasExecutorComputationApplication.class, args);
	}
}

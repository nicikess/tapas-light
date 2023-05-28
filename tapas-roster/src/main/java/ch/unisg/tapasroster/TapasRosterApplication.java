package ch.unisg.tapasroster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "ch.unisg.tapasroster", "ch.unisg.tapascommon" })
public class TapasRosterApplication {

	public static void main(String[] args) {
		SpringApplication.run(TapasRosterApplication.class, args);
	}
}

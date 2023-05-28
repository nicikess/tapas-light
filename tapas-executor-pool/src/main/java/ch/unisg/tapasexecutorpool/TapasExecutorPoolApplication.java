package ch.unisg.tapasexecutorpool;

import ch.unisg.tapasexecutorpool.pool.adapter.out.persistence.mongodb.ExecutorRepository;
import ch.unisg.tapasexecutorpool.pool.adapter.out.persistence.mongodb.TaskAssignmentRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = { ExecutorRepository.class, TaskAssignmentRepository.class })
@ComponentScan(basePackages = { "ch.unisg.tapasexecutorpool", "ch.unisg.tapascommon" })
public class TapasExecutorPoolApplication {

	public static void main(String[] args) {
		SpringApplication.run(TapasExecutorPoolApplication.class, args);
	}
}

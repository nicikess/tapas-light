package ch.unisg.tapasexecutorbase.executor.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ExecutorConfig {

    @Getter
    @Value("${executor.id}")
    private String executorId;

    @Getter
    @Value("${executor.name}")
    private String executorName;

    @Getter
    @Value("${executor.type}")
    private String executorType;
}

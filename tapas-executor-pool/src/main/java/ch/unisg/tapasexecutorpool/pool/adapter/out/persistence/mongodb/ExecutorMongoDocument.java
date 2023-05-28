package ch.unisg.tapasexecutorpool.pool.adapter.out.persistence.mongodb;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "executors")
@AllArgsConstructor
public class ExecutorMongoDocument {
    @Id private String executorId;
    private String executorName;
    private String executorType;
    private String executorAddress;
    private String executorState;
    private String executorPoolName;
}

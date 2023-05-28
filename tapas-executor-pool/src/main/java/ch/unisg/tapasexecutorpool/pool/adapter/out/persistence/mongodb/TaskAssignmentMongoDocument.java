package ch.unisg.tapasexecutorpool.pool.adapter.out.persistence.mongodb;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "task_assignments")
@AllArgsConstructor
public class TaskAssignmentMongoDocument {
    @Id private String taskId;
    private String executorId;
}

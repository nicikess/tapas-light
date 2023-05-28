package ch.unisg.tapastasks.tasks.adapter.out.persistence.mongodb;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "tasks")
@AllArgsConstructor
public class MongoTaskDocument {
    @Id private String taskId;
    private String taskName;
    private String taskType;
    private String originalTaskUri;
    private String taskStatus;
    private String provider;
    private String inputData;
    private String outputData;
    private String taskListName;
}

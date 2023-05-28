package ch.unisg.tapasexecutorpool.pool.adapter.out.persistence.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExecutorRepository extends MongoRepository<ExecutorMongoDocument, String> {
    ExecutorMongoDocument findByExecutorId(String taskId, String taskListName);
    List<ExecutorMongoDocument> findByExecutorPoolName(String taskListName);
}

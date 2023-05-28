package ch.unisg.tapasexecutorpool.pool.adapter.out.persistence.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskAssignmentRepository extends MongoRepository<TaskAssignmentMongoDocument, String> {
}

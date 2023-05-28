package ch.unisg.tapasexecutorpool.pool.adapter.out.persistence.mongodb;

import ch.unisg.tapasexecutorpool.pool.application.port.out.AddTaskAssignmentToRepositoryPort;
import ch.unisg.tapasexecutorpool.pool.domain.TaskAssignment;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskAssignmentPersistenceAdapter implements AddTaskAssignmentToRepositoryPort {

    private static final Logger LOGGER = LogManager.getLogger(TaskAssignmentPersistenceAdapter.class);

    private final TaskAssignmentRepository repository;
    private final TaskAssignmentMapper mapper;

    @Override
    public void addTaskAssignment(TaskAssignment taskAssignment) {
        try {
            var document = mapper.mapToMongoDocument(taskAssignment);
            repository.save(document);
            LOGGER.info("Saved TaskAssignment to MongoDB");
        } catch (Exception e) {
            LOGGER.warn("Failed to add TaskAssignment to MongoDB");
        }
    }
}

package ch.unisg.tapasexecutorpool.pool.adapter.out.persistence.mongodb;

import ch.unisg.tapascommon.pool.domain.Executor;
import ch.unisg.tapascommon.tasks.domain.Task;
import ch.unisg.tapasexecutorpool.pool.domain.TaskAssignment;
import org.springframework.stereotype.Component;

@Component
public class TaskAssignmentMapper {
    TaskAssignment mapToDomainEntity(TaskAssignmentMongoDocument taskAssignment) {
        return new TaskAssignment(
                new Task.TaskId(taskAssignment.getTaskId()),
                new Executor.ExecutorId(taskAssignment.getExecutorId())
        );
    }

    TaskAssignmentMongoDocument mapToMongoDocument(TaskAssignment taskAssignment) {
        return new TaskAssignmentMongoDocument(
                taskAssignment.getTaskId().getValue(),
                taskAssignment.getExecutorId().getValue()
        );
    }
}

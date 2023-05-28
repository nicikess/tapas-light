package ch.unisg.tapasexecutorpool.pool.application.port.out;

import ch.unisg.tapasexecutorpool.pool.domain.TaskAssignment;

public interface AddTaskAssignmentToRepositoryPort {
    void addTaskAssignment(TaskAssignment taskAssignment);
}

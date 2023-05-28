package ch.unisg.tapasexecutorpool.pool.domain;

import ch.unisg.tapascommon.pool.domain.Executor;
import ch.unisg.tapascommon.tasks.domain.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class TaskAssignment {
    @Getter
    private Task.TaskId taskId;

    @Getter
    private Executor.ExecutorId executorId;
}

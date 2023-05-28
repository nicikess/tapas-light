package ch.unisg.tapastasks.tasks.application.port.in;

import ch.unisg.tapascommon.common.SelfValidating;
import ch.unisg.tapascommon.tasks.domain.Task.TaskId;
import lombok.EqualsAndHashCode;
import lombok.Value;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Value
public class RetrieveTaskFromTaskListQuery extends SelfValidating<RetrieveTaskFromTaskListQuery> {
    @NotNull
    TaskId taskId;

    public RetrieveTaskFromTaskListQuery(TaskId taskId) {
        this.taskId = taskId;
        this.validateSelf();
    }
}

package ch.unisg.tapastasks.tasks.adapter.out.persistence.mongodb;

import ch.unisg.tapascommon.tasks.domain.Task;
import ch.unisg.tapastasks.tasks.domain.TaskList;
import org.springframework.stereotype.Component;

@Component
class TaskMapper {
    Task mapToDomainEntity(MongoTaskDocument task) {
        return new Task(
            new Task.TaskId(task.getTaskId()),
            new Task.TaskName(task.getTaskName()),
            new Task.TaskType(task.getTaskType()),
            new Task.OriginalTaskUri(task.getOriginalTaskUri()),
            new Task.TaskStatus(Task.Status.valueOf(task.getTaskStatus())),
            new Task.ServiceProvider(task.getProvider()),
            new Task.InputData(task.getInputData()),
            new Task.OutputData(task.getOutputData())
        );
    }

    MongoTaskDocument mapToMongoDocument(Task task) {
        return new MongoTaskDocument(
            task.getTaskId().getValue(),
            task.getTaskName().getValue(),
            task.getTaskType().getValue(),
            task.getOriginalTaskUri().getValue(),
            task.getTaskStatus().getValue().toString(),
            task.getProvider().getValue(),
            task.getInputData().getValue(),
            task.getOutputData().getValue(),
            TaskList.getTapasTaskList().getTaskListName().getValue()
        );
    }
}
